package com.study.touchandgesturetest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.study.touchandgesturetest.quadrant.ExperimentListener;
import com.study.touchandgesturetest.quadrant.ExperimentQuadrant;
import com.study.touchandgesturetest.quadrant.ExperimentRepository;
import com.study.touchandgesturetest.quadrant.ExperimentRepository.Experiment;
import com.study.touchandgesturetest.quadrant.GestureDetector;

public class GestureExperimentActivity extends Activity
implements ExperimentListener {

	private static final String GESTURE_TAG = "Gesture";

	private static final String EXPERIMENT = "Expermiment";

	private GestureExperimentView experimentView;
	
	private GestureDetectorCompat mDetector;
	
	private List<PointF> gesturePoints;

	private ExperimentRepository experimentRepository;

	private PointerCoords outPointerCoords;

	private ExperimentLogger experimentLogger;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set full screen mode
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_gesture_experiment);
		
		// Setup Experiment repository
		experimentRepository = new ExperimentRepository();
		
		// Get screen width and height
		DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        Log.d("GestureExperimentActivity", "Screen " + width + "x" + height);
		
        // Setup the ExperimentView
		experimentView = (GestureExperimentView) findViewById(R.id.experimentView);
		experimentView.setDimension(width, height);
		experimentView.setExperimentListener(this);
		
		mDetector = new GestureDetectorCompat(this, 
				experimentView.getTouchListenter());
		
		gesturePoints = new ArrayList<PointF>(100);
		outPointerCoords = new PointerCoords();
		
		// Get userNo and using type from the Startup activity
		Intent intent = getIntent();
		
		// Record experiment result
		int userNo = intent.getIntExtra(StartupActivity.EXTRA_USER_NO, 0);
		String usingType = intent.getStringExtra(StartupActivity.EXTRA_USING_TYPE);
		experimentLogger = new ExperimentLogger(userNo, usingType);

		// Start an experiment session
		nextExperiment();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (mDetector.onTouchEvent(event))
			return super.onTouchEvent(event);
		
		ExperimentQuadrant activeQuadrant = experimentView.getActiveQuadrant();
		if (activeQuadrant == null)
			return super.onTouchEvent(event);

		int index = event.getActionIndex();
		int action = event.getActionMasked();
		
		event.getPointerCoords(index, outPointerCoords);
		if (!activeQuadrant.getBoundary().contains(
				outPointerCoords.x, outPointerCoords.y))
			return super.onTouchEvent(event);

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			break;
			
		case MotionEvent.ACTION_MOVE:
			PointF v = new PointF(outPointerCoords.x, outPointerCoords.y);
			gesturePoints.add(v);
			Log.d(GESTURE_TAG, "Gesture point (" + v.x + "," + v.y + ")");
			break;
			
		case MotionEvent.ACTION_UP:
			Log.d(GESTURE_TAG, "Up");
			if (gesturePoints.size() >= GestureDetector.MIN_NUM_POINTS) {
				activeQuadrant.detectMovement(gesturePoints);
			}
		case MotionEvent.ACTION_CANCEL:
			gesturePoints.clear();
			break;
		}
		
		return super.onTouchEvent(event);
	}

	@Override
	public void onExperimentCorrect(int quadrantNo, int experimentId) {
		onExperimentEnd(quadrantNo, experimentId, "Success", ExperimentLogger.SUCCESS);
	}

	@Override
	public void onExperimentError(int quadrantNo, int experimentId, String reason) {
		String result = (reason != null) ? reason : "Error";
		onExperimentEnd(quadrantNo, experimentId, result, ExperimentLogger.ERROR);
	}

	@Override
	public void onExperimentTimeout(int quadrantNo, int experimentId) {
		onExperimentEnd(quadrantNo, experimentId, "Timeout", ExperimentLogger.TIMEOUT);
	}
	
	private void onExperimentEnd(int quadrantNo, int experimentId,
			String resultStr, int resultCode) {
		
		Log.d(EXPERIMENT, resultStr + " at Quadrant " + quadrantNo + ": buttonId/signId = " + experimentId);
		
		Toast.makeText(this, resultStr, Toast.LENGTH_SHORT).show();
		experimentLogger.endExperiment(quadrantNo, experimentId, resultCode, resultStr);
		
		nextExperiment();
	}
	
	private void nextExperiment() {
		
		if (experimentRepository.hashNextExperiment()) {
			experimentLogger.startExperiment();
			
			Experiment experiment = experimentRepository.nextExperiment();
			experimentView.nextExperiment(experiment);
			experimentView.invalidate();
			
		} else {

			// Save experiment result
			String text;
			try {
				String savedFile = experimentLogger.save();
				text = "Result saved to " + savedFile;
			} catch (IOException e) {
				text = "Failed to save result: " + e.getMessage();
			}
			Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	    	
			// End of experiment - Start the ExperimentResult activity
			Intent intent = new Intent(this, ExperimentResultActivity.class);
	    	startActivity(intent);
		}
	}

}
