package com.meximas.wcr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddEvents extends Activity
{

	private EditText dayInput;
	private EditText monthInput;
	private EditText yearInput;
	private boolean flag = false;
	private EditText descr;

	private String eventToAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_events);
		setTitle("World Championships Reminder");

		/**
		 * Validation
		 */
		Button validFormBtn = (Button) findViewById(R.id.addEvent);
		validFormBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				validate();
				if (flag == true)
				{
					Intent intentMainActivity = getIntent();
					Bundle bundle = new Bundle();
					bundle = intentMainActivity.getExtras();
					if (bundle == null)
					{
						ArrayList<String> userEvents = new ArrayList<String>();
						userEvents.add(eventToAdd);
						Intent intent = new Intent(AddEvents.this, TimeCounter.class);
						intent.putStringArrayListExtra("userEvents", userEvents);
						startActivity(intent);
						finish();
					} else
					{
						ArrayList<String> userEvents = bundle.getStringArrayList("userEvents");
						userEvents.add(eventToAdd);
						Intent intent = new Intent(AddEvents.this, TimeCounter.class);
						intent.putStringArrayListExtra("userEvents", userEvents);
						startActivity(intent);
						finish();
					}
				}
			}
		});
	}

	/**
	 * Method is validating fields: day,month,year,name of Event.
	 * 
	 * @author Retman
	 */
	private void validate()
	{
		dayInput = (EditText) findViewById(R.id.dayInput);
		monthInput = (EditText) findViewById(R.id.monthInput);
		yearInput = (EditText) findViewById(R.id.yearInput);
		descr = (EditText) findViewById(R.id.nameOfEvent);
		String day = dayInput.getText().toString();
		String month = monthInput.getText().toString();
		String year = yearInput.getText().toString();
		String desc = descr.getText().toString();
		if (dayInput.length() > 2 || Integer.valueOf(day) > 31 || (Integer.valueOf(day) > 29 && monthInput.getText().toString() == "02"))
		{
			generateErrorDialog();
		} else if (monthInput.length() > 2 || Integer.valueOf(month) > 12)
		{
			generateErrorDialog();
		} else if (Integer.valueOf(year) < Calendar.getInstance().get(Calendar.YEAR))
		{
			generateErrorDialog();
		} else if (descr.length() == 0)
		{
			generateErrorDialog();
		} else
		{
			eventToAdd = "M. " + desc + ", Year: " + year + ", Month: " + month + ", Day: " + day + ";";
			flag = true;
		}
	}

	/**
	 * Method is generating Alert Dialog
	 * 
	 * @author Retman
	 */
	private void generateErrorDialog()
	{
		AlertDialog.Builder msgDialog = new AlertDialog.Builder(AddEvents.this);

		msgDialog.setTitle(R.string.messageAlertTitleAddEvent);
		msgDialog.setMessage(R.string.messageAlertAddEvent);
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
