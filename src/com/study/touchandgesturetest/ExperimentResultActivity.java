package com.study.touchandgesturetest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class ExperimentResultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experiment_result);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.experiment_result, menu);
		return true;
	}

	public void reset(View view) {
		Intent intent = new Intent(this, StartupActivity.class);
		startActivity(intent);
	}
	
	public void exit(View view) {
		Intent intent = new Intent(this, StartupActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(StartupActivity.EXTRA_EXIT, true);
		startActivity(intent);
	}
}
