package com.meximas.wcr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

@SuppressLint("SimpleDateFormat")
public class Timer extends Activity
{

	List<String> userEvents = new ArrayList<String>();
	String year;
	String month;
	String day;
	LinkedList<Date> userDatesAndEventsMap = new LinkedList<Date>();
	LinkedList<Long> userCountedTimeList = new LinkedList<Long>();
	private Handler timerHandler = new Handler();
	protected int timeTickDown = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		List<String> userEvents = bundle.getStringArrayList("userEvents");
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
			String fullDate = year + "." + month + "." + day;
			try
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy.mm.dd");
				userDatesAndEventsMap.add(i, sdf.parse(fullDate));
			} catch (ParseException e)
			{
				Log.d("Parse Exception", e.toString());
			}
		}
		this.userDatesAndEventsMap = userDatesAndEventsMap;
		countTime();

		for (int i = 0; i <= userCountedTimeList.size() - 1; i++)
		{
			long dayHoursMinutes = userCountedTimeList.get(0);
			long x = dayHoursMinutes - 1;
			TimerTask timerTask = new TimerTask()
			{

				@Override
				public void run()
				{
					// TODO Auto-generated method stub

				}
			};
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);
	}

	// Override this in Subclass to get or add specific tick behaviors
	protected void myTickTask()
	{
		if (timeTickDown == 0)
		{
			timerHandler.post(doUpdateTimeout);
		}
		timeTickDown--;

	}

	private Runnable doUpdateTimeout = new Runnable()
	{
		public void run()
		{
			updateTimeout();
		}
	};

	private void updateTimeout()
	{
		timeTickDown = 10;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void countTime()
	{
		TimeUnit timeUnit = TimeUnit.DAYS;
		Date dateOne;
		Date dateTwo;
		Calendar cal = Calendar.getInstance();
		dateOne = cal.getTime();
		for (int i = 0; i <= userDatesAndEventsMap.size() - 1; i++)
		{
			dateTwo = userDatesAndEventsMap.get(i);
			long diffInMillies = dateTwo.getTime() - dateOne.getTime();
			long convert = timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
			userCountedTimeList.add(i, convert);
		}
	}
}