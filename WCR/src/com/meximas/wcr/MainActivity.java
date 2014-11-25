package com.meximas.wcr;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("World Championships Reminder");
		/**
		 * Setting font type to TextView
		 */
		TextView headerTextView = (TextView) findViewById(R.id.hidden_value);
		Typeface type = Typeface.createFromAsset(getAssets(), "SPORT_CENTER.ttf");
		headerTextView.setTypeface(type);
		TextView version = (TextView) findViewById(R.id.monthTextView);
		version.setTypeface(type);
		/**
		 * Add to reminder
		 */
		Button addToReminder = (Button) findViewById(R.id.addToReminder);
		addToReminder.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intentMainActivity = getIntent();
				Intent intent = new Intent(MainActivity.this, AddEvents.class);
				Bundle bundle = new Bundle();
				bundle = intentMainActivity.getExtras();
				if (bundle == null)
				{
					startActivity(intent);
				} else
				{
					ArrayList<String> userEvents = bundle.getStringArrayList("userEvents");
					intent.putStringArrayListExtra("userEvents", userEvents);
					startActivity(intent);
				}
			}
		});
		/**
		 * Button to Activity = Choser
		 */
		Button choosingGamesButton = (Button) findViewById(R.id.chooseGame);
		choosingGamesButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intentMainActivity = getIntent();
				Bundle bundle = new Bundle();
				bundle = intentMainActivity.getExtras();
				if (bundle != null)
				{
					ArrayList<String> userEvents = bundle.getStringArrayList("userEvents");
					Intent intent = new Intent(MainActivity.this, Choser.class);
					intent.putStringArrayListExtra("userEvents", userEvents);
					startActivity(intent);
					finish();
				} else
				{
					Intent intent = new Intent(MainActivity.this, Choser.class);
					startActivity(intent);
					finish();
				}
			}
		});

		/**
		 * Checking current Events
		 */
		Button checkMyCurrentEvents = (Button) findViewById(R.id.checkMyChosenEvents);
		checkMyCurrentEvents.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intentMainActivity = getIntent();
				Bundle bundle = new Bundle();
				bundle = intentMainActivity.getExtras();
				try
				{
					ArrayList<String> userEvents = bundle.getStringArrayList("userEvents");
					Intent intent = new Intent(MainActivity.this, TimeCounter.class);
					intent.putStringArrayListExtra("userEvents", userEvents);
					intent.putExtra("isThreadTimerSet", true);
					startActivity(intent);
					finish();
				} catch (NullPointerException e)
				{

					AlertDialog.Builder msgDialog = new AlertDialog.Builder(MainActivity.this);

					msgDialog.setTitle(R.string.messageAlertTitle);
					msgDialog.setMessage(R.string.messageAlert);
					msgDialog.setNeutralButton("OK", new DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
						}
					});
					AlertDialog alertDialog = msgDialog.create();
					alertDialog.show();
				}

			}
		});
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();

		final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		alert.setTitle(R.string.mainActivityBackPressedTitle).setMessage(R.string.mainActivityBackPressedMessage)

		// Positive is closing application
				.setPositiveButton(R.string.mainActivityBackPressedPositiveBtn, new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Intent mainActivity = new Intent(Intent.ACTION_MAIN);
						mainActivity.addCategory(Intent.CATEGORY_HOME);
						mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						dialog.dismiss();
						startActivity(mainActivity);
						MainActivity.this.finish();
					}
				})

				// Negative is starting mainActivity (main screen after device
				// reboot)
				// but not close application
				.setNegativeButton(R.string.mainActivityBackPressedNegativeBtn, new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						final Intent mainActivity = new Intent(Intent.ACTION_MAIN);
						mainActivity.addCategory(Intent.CATEGORY_HOME);
						mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(mainActivity);
						dialog.dismiss();
					}
				});

		alert.create();
		alert.show();
	}
}
