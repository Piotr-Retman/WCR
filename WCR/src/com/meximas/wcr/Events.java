package com.meximas.wcr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.meximas.wcr.sports.Sport;

public class Events extends Activity
{

	Sport sport = new Sport();
	Set<String> choosedByUserEvents = new HashSet<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events);
		setTitle("World Championships Reminder");
		final ListView listView = (ListView) findViewById(R.id.list_time);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		List<String> eventsToDisplay = bundle.getStringArrayList("eventsToDisplay");
		int size = eventsToDisplay.size();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
		for (int i = 0; i < size; i++)
		{
			// Code to add choosable elements of ListView
			adapter.add(eventsToDisplay.get(i).toString());
		}
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				if (listView.isItemChecked(arg2))
				{
					choosedByUserEvents.add(listView.getItemAtPosition(arg2).toString());
					listView.setItemChecked(arg2, true);
					arg1.setBackgroundColor(Color.BLACK);
				} else
				{
					arg1.setBackgroundColor(Color.TRANSPARENT);
					listView.setItemChecked(arg2, false);
					choosedByUserEvents.remove(listView.getItemAtPosition(arg2).toString());
				}
			}
		});

		// Going setting User Events and going to the next Activity
		Button buttonForward = (Button) findViewById(R.id.set_events);
		buttonForward.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				sport.setUserEvents(choosedByUserEvents);

				Intent intentMainActivity = getIntent();
				Bundle bundle;
				bundle = intentMainActivity.getExtras();
				if (bundle.getStringArrayList("userEvents") != null)
				{
					Set<String> userEventsThis = sport.getUserEvents();
					ArrayList<String> userEvents = bundle.getStringArrayList("userEvents");
					userEvents.addAll(userEventsThis);
					Intent intent = new Intent(Events.this, TimeCounter.class);
					intent.putStringArrayListExtra("userEvents", userEvents);
					intent.putExtra("isThreadTimerSet", true);
					startActivity(intent);
					finish();
				}else{
					Set<String> userEventsThis = sport.getUserEvents();
					ArrayList<String> localListOfUserEvents = new ArrayList<String>(userEventsThis);
					Intent intent = new Intent(Events.this, TimeCounter.class);
					intent.putStringArrayListExtra("userEvents", localListOfUserEvents);
					startActivity(intent);
					finish();
				}
			}
		});
	}

	@Override
	public void onBackPressed()
	{
		// Write your code here
		finish();
		Intent intent = new Intent(Events.this, Choser.class);
		startActivity(intent);
		super.onBackPressed();
	}

}
