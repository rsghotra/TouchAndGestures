package com.study.touchandgesturetest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class StartupActivity extends Activity {

	public static final String EXTRA_USER_NO = "com.study.touchandgesturetest.USER_NO";
	public static final String EXTRA_USING_TYPE = "com.study.touchandgesturetest.USING_TYPE";
	public static final String EXTRA_EXIT = "com.study.touchandgesturetest.EXIT";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        
        // Populate spinner's entries
        setSpinnerAdapter((Spinner) findViewById(R.id.spinner_user_no),
        		R.array.user_nos);
        setSpinnerAdapter((Spinner) findViewById(R.id.spinner_using_type),
        		R.array.using_types);
        
        if (getIntent().getBooleanExtra(EXTRA_EXIT, false)) {
        	finish();
        }
    }
	
	@Override
	public void onBackPressed() {
		finish();
	}

	private void setSpinnerAdapter(Spinner spinner, int arrayId) {
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				arrayId, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**
     * Called when the user clicks the "Start" button
     * @param view
     */
    public void startExperiment(View view) {
    	
    	// Intent to start the GestureExperiment activity
    	Intent intent = new Intent(this, GestureExperimentActivity.class);
    	
    	Spinner spinner;
    	
    	// Pass userNo to the GestureExperiment activity
    	spinner = (Spinner) findViewById(R.id.spinner_user_no);
    	int userNo = Integer.valueOf((String) spinner.getSelectedItem());
    	intent.putExtra(EXTRA_USER_NO, userNo);
    	
    	// Pass usingTyle to the GestureExperiment activity
    	spinner = (Spinner) findViewById(R.id.spinner_using_type);
    	String usingType = (String) spinner.getSelectedItem();
    	intent.putExtra(EXTRA_USING_TYPE, usingType);
    	
    	// Start the GestureExperiment activity
    	startActivity(intent);
    }
}
