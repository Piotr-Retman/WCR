package com.meximas.wcr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressLint("SimpleDateFormat")
public class TimeCounter extends Activity
{

	ArrayList<String> userEvents = new ArrayList<String>();
	String year;
	String month;
	String day;
	LinkedList<Date> userDatesAndEventsMap = new LinkedList<Date>();
	LinkedList<String> userCountedTimeList = new LinkedList<String>();
	protected int timeTickDown = 1;
	ListView listView;
	long second = 0;
	private ArrayAdapter<String> adapter;
	ThreadGroup tg = new ThreadGroup("CountingThreads");
	private ArrayList<String> mData = new ArrayList<String>();
	private ArrayList<String> userEventsLine = new ArrayList<String>();
	private LinkedList<String> userDatesEvents = new LinkedList<String>();
	private String fullDate;
	private HashMap<Integer, Thread> myActiveThreadMap = new HashMap<Integer, Thread>();
	private Boolean isThreadTimerSet;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);
		setTitle("World Championships Reminder");
		// Getting userEvents from previous Activity
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		ArrayList<String> userEvents = bundle.getStringArrayList("userEvents");
		this.userEvents = userEvents;

		LinkedList<Date> userDatesAndEventsMap = new LinkedList<Date>();

		// Creating dates from userEvents list
		for (int i = 0; i < userEvents.size(); i++)
		{
			String listElement = userEvents.get(i).toString();

			for (int j = 0; j < listElement.length(); j++)
			{
				if (j + 7 < listElement.length())
				{
					String yearSentence = listElement.substring(j, j + 6);
					String monthSentence = listElement.substring(j, j + 7);
					String daySentence = listElement.substring(j, j + 5);

					if (yearSentence.equals("Year: "))
					{

						if (j + 6 < listElement.length() && j + 10 < listElement.length())
						{
							year = listElement.substring(j + 6, j + 10).toString();
						}
					}
					if (monthSentence.equals("Month: "))
					{

						if (j + 7 < listElement.length() && j + 9 < listElement.length())
						{
							month = listElement.substring(j + 7, j + 9).toString();
						}
					}
					if (daySentence.equals("Day: "))
					{

						if (j + 5 < listElement.length() && j + 7 < listElement.length())
						{
							day = listElement.substring(j + 5, j + 7).toString();
						}
					}
				}

			}
			if (month.equals("nu"))
			{
				month = "01";
			}
			if (day.equals("nu"))
			{
				day = "01";
			}
			fullDate = year + "." + month + "." + day;
			userDatesEvents.add(i, fullDate);
			try
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
				userDatesAndEventsMap.add(i, sdf.parse(fullDate));
			} catch (ParseException e)
			{
				Log.d("Parse Exception", e.toString());
			}
		}
		this.userDatesAndEventsMap = userDatesAndEventsMap;
		countTime();

		listView = (ListView) findViewById(R.id.list_time);
		// adapter = new ArrayAdapter<String>(getApplicationContext(),
		// android.R.layout.simple_list_item_1,mData);

		// Loop is adding timers into listView
		returnNormalLineUserEvent();
		for (int i = 0; i < userCountedTimeList.size(); i++)
		{
			mData.add(userDatesEvents.get(i).toString() + " - " + userEventsLine.get(i).toString() + " - " + userCountedTimeList.get(i).toString());
		}
		adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, mData);
		listView.setAdapter(adapter);

		Boolean isThreadTimer = getIntent().getExtras().getBoolean("isThreadTimerSet");
		isThreadTimerSet = isThreadTimer;
		if (isThreadTimerSet.equals(false))
		{
			Thread t = new Thread(tg, new Runnable()
			{

				@Override
				public void run()
				{
					for (;;)
					{
						countTimeCollapsed();
					}
				}
			}, "Time");
			t.start();

		}
		final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
		{

			@Override
			public void onRefresh()
			{
				swipeRefreshLayout.setRefreshing(true);
				(new Handler()).postDelayed(new Runnable()
				{

					@Override
					public void run()
					{
						returnNormalLineUserEvent();
						for (int i = 0; i < mData.size(); i++)
						{
							mData.set(i, userDatesEvents.get(i).toString() + " - " + userEventsLine.get(i).toString() + " - " + userCountedTimeList.get(i).toString());
						}
						adapter.notifyDataSetChanged();

					}
				}, 1000);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author Retman
	 */
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		Intent intent = new Intent(TimeCounter.this, MainActivity.class);
		intent.putStringArrayListExtra("userEvents", userEvents);
		intent.putExtra("isThreadTimerSet", isThreadTimerSet);
		startActivity(intent);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author Retman
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		int base = Menu.FIRST;
		menu.add(base, base, base, R.string.goToMain);
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author Retman
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case 1:
				item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
				{

					@Override
					public boolean onMenuItemClick(MenuItem arg0)
					{
						Intent intent = new Intent(TimeCounter.this, MainActivity.class);
						intent.putStringArrayListExtra("userEvents", userEvents);
						intent.putExtra("isThreadTimerSet", isThreadTimerSet);
						startActivity(intent);
						return false;
					}
				});
				break;
		}
		return true;
	}

	private void refresh()
	{
		countTime();
	}

	/**
	 * Method is retrieving time in days.
	 * 
	 * @author Retman
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void countTime()
	{
		TimeUnit days = TimeUnit.DAYS;
		Date dateOne;
		Date dateTwo;
		Calendar cal = Calendar.getInstance();

		for (int i = 0; i <= userDatesAndEventsMap.size() - 1; i++)
		{
			dateOne = cal.getTime();
			dateTwo = userDatesAndEventsMap.get(i);
			long diffInMillies = dateTwo.getTime() - dateOne.getTime();
			long dayLeft = days.convert(diffInMillies, TimeUnit.MILLISECONDS);
			long countDay = dayLeft;
			if (countDay == 0)
			{
				countDay = 0;
			} else
			{
				countDay = dayLeft - 1;
			}
			// Count left minutes
			long currentHour = cal.get(Calendar.HOUR_OF_DAY);
			long currentMinute = cal.get(Calendar.MINUTE);
			long hourLeft = 23 - currentHour;
			long minutesLeft = 59 - currentMinute;
			String currentTime = String.valueOf(countDay) + "d" + hourLeft + "h" + minutesLeft + "m";
			// End count of left minutes
			userCountedTimeList.add(i, currentTime);
		}

		checkIfTimeToRemind();
	}

	/**
	 * Method is checking if it is time to remind user that event start to happen.
	 */
	public void checkIfTimeToRemind()
	{
		for (int i = 0; i <= userCountedTimeList.size() - 1; i++)
		{
			
		}
	}

	/**
	 * Method is returning full name of Event.
	 * 
	 * @author Retman
	 */
	private List<String> returnNormalLineUserEvent()
	{
		for (int l = 0; l < userEvents.size(); l++)
		{
			String value = userEvents.get(l).toString();
			for (int i = 0; i < value.length(); i++)
			{
				String val = value.substring(i, i + 1).toString();
				if (val.equals(","))
				{
					userEventsLine.add(value.substring(3, i));
					break;
				}
			}
		}
		return userEventsLine;
	}

	/**
	 * Method count time collapsed
	 * 
	 * @author Retman
	 */
	@SuppressLint("NewApi")
	private void countTimeCollapsed()
	{
		try
		{
			second += 1;
			Thread.sleep(1000);
			if (second == 59)
			{
				second = 0;
				refresh();
			}
		} catch (Exception e)
		{
			Log.d("myapp", Log.getStackTraceString(e));
		}
	}
}