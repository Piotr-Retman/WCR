package com.meximas.wcr;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

import com.meximas.wcr.sports.Sport;

public class Choser extends Activity
{
	List<String> listOfSports = new ArrayList<String>();
	Sport sport = new Sport();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choser);
		setTitle("World Championships Reminder");
		Button addToReminder = (Button) findViewById(R.id.addToReminder);

		addToReminder.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				CheckBox cycling = (CheckBox) findViewById(R.id.cycling);
				if (cycling.isChecked())
				{
					listOfSports.add("cycling");
				}
				CheckBox tennis = (CheckBox) findViewById(R.id.tennis);
				if (tennis.isChecked())
				{
					listOfSports.add("tennis");
				}
				CheckBox soccer = (CheckBox) findViewById(R.id.soccer);
				if (soccer.isChecked())
				{
					listOfSports.add("soccer");
				}
				CheckBox volleyball = (CheckBox) findViewById(R.id.volleyball);
				if (volleyball.isChecked())
				{
					listOfSports.add("volleyball");
				}
				CheckBox table_tennis = (CheckBox) findViewById(R.id.tableTennis);
				if (table_tennis.isChecked())
				{
					listOfSports.add("table_tennis");
				}
				CheckBox swimming = (CheckBox) findViewById(R.id.swimming);
				if (swimming.isChecked())
				{
					listOfSports.add("swimming");
				}
				CheckBox handball = (CheckBox) findViewById(R.id.handball);
				if (handball.isChecked())
				{
					listOfSports.add("handball");
				}
				CheckBox formula_one = (CheckBox) findViewById(R.id.formula);
				if (formula_one.isChecked())
				{
					listOfSports.add("formula_one");
				}
				CheckBox ski_jumping = (CheckBox) findViewById(R.id.skiJump);
				if (ski_jumping.isChecked())
				{
					listOfSports.add("ski_jumping");
				}
				CheckBox figure_skating = (CheckBox) findViewById(R.id.figureSkat);
				if (figure_skating.isChecked())
				{
					listOfSports.add("figure_skating");
				}
				CheckBox my_events = (CheckBox) findViewById(R.id.myEvents);
				if (my_events.isChecked())
				{
					listOfSports.add("my");
				}
				sport.setUserDisciplines(listOfSports);

				// Place to add method which is taking data from file and show
				try
				{
					listOfUserDisciplinesAndEvents();
					// up in the next
					Intent intent = new Intent(Choser.this, Events.class);
					intent.putStringArrayListExtra("eventsToDisplay", (ArrayList<String>) sport.getEventsToDisplay());
					Intent intentMainActivity = getIntent();
					Bundle bundle = new Bundle();
					bundle = intentMainActivity.getExtras();
					if (bundle != null)
					{
						ArrayList<String> userEvents = bundle.getStringArrayList("userEvents");
						intent.putStringArrayListExtra("userEvents", userEvents);
						intent.putExtra("isThreadTimerSet", true);
					}
					startActivity(intent);
					finish();
				} catch (IOException e)
				{
					Log.d("myapp", Log.getStackTraceString(new Exception()));
				}
			}
		});
	}

	/**
	 * Method which is supporting File in Assets
	 */
	public void getListsOfEvents() throws IOException
	{
		Map<String, ArrayList<String>> listOfSportEventsLists = new HashMap<String, ArrayList<String>>();
		AssetManager assets = getAssets();
		InputStream file = assets.open("WRC.txt");
		Scanner fileRead = new Scanner(new InputStreamReader(file));
		String line = null;
		while (fileRead.hasNext())
		{
			line = fileRead.nextLine();
			if (!line.startsWith("*"))
			{
				if (line.startsWith("Discipline: Soccer") && fileRead.hasNext())
				{
					ArrayList<String> soccerEvents = new ArrayList<String>();
					for (int i = 0;;)
					{
						if (fileRead.hasNext("-"))
						{
							break;
						} else
						{
							soccerEvents.add(i, fileRead.nextLine());
							i++;
						}
					}
					listOfSportEventsLists.put("soccer", soccerEvents);

				}
				if (line.startsWith("Discipline: My") && fileRead.hasNext())
				{
					ArrayList<String> myEvents = new ArrayList<String>();
					for (int i = 0;;)
					{
						if (fileRead.hasNext("-"))
						{
							break;
						} else
						{
							myEvents.add(i, fileRead.nextLine());
							i++;
						}
					}
					listOfSportEventsLists.put("my", myEvents);
				}
			} else
			{
				break;
			}
			sport.setListOfSportEventsLists(listOfSportEventsLists);
		}
		fileRead.close();

	}

	/**
	 * 
	 * Method is setting the User Disciplines and Events
	 * 
	 */
	public void listOfUserDisciplinesAndEvents() throws IOException
	{
		// method below is taking the list of All events from file
		getListsOfEvents();
		List<String> userDisciplines = sport.getUserDisciplines();
		List<String> eventsToDisplay = new ArrayList<String>(userDisciplines.size());
		for (int i = 0; i < userDisciplines.size(); i++)
		{
			switch (userDisciplines.get(i).toString())
			{
				case "soccer":
					eventsToDisplay.addAll(i, sport.getListOfSportEventsLists().get("soccer"));
					break;
				case "my":
					eventsToDisplay.addAll(i, sport.getListOfSportEventsLists().get("my"));
					break;
				default:
					eventsToDisplay.add("none");
					break;
			}
		}
		sport.setEventsToDisplay(eventsToDisplay);
	}
}
