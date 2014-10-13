package com.meximas.wcr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/**
		 * Button to Activity = Choser
		 */
		Button choosingGamesButton = (Button) findViewById(R.id.chooseGame);
		choosingGamesButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,Choser.class);
				startActivity(intent);
			}
		});
		
		/**
		 * Button to Activity = KnowMore
		 */
		Button learnMore = (Button) findViewById(R.id.learnMore);
		learnMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,KnowMore.class);
				startActivity(intent);
			}
		});
	}
	
}
