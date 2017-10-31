package com.study.touchandgesturetest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;

import com.study.touchandgesturetest.quadrant.ButtonsQuadrant;
import com.study.touchandgesturetest.quadrant.ExperimentListener;
import com.study.touchandgesturetest.quadrant.ExperimentQuadrant;
import com.study.touchandgesturetest.quadrant.ExperimentRepository;
import com.study.touchandgesturetest.quadrant.GestureQuadrant;

public class GestureExperimentView extends View {

	private Paint backgroundPaint = new Paint();
	
	private ButtonsQuadrant buttonsQuadrant;
	
	private GestureQuadrant gestureQuadrant;
	
	private ExperimentQuadrant activeQuadrant;
	
	private int width;
	
	private int height;
	
	public GestureExperimentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(true);
		setFocusableInTouchMode(true);
		
		backgroundPaint.setColor(Color.BLACK);
		backgroundPaint.setAntiAlias(true);
		
		// Create two types of quadrants
		buttonsQuadrant = new ButtonsQuadrant();
		gestureQuadrant = new GestureQuadrant();

		activeQuadrant = null;
	}

	public GestureExperimentView(Context context) {
		this(context, null);
	}

	public void setDimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void nextExperiment(ExperimentRepository.Experiment experiment) {
		
		// Deactivate the current active quadrant
		if (activeQuadrant != null)
			activeQuadrant.nextExperiment(ExperimentConstants.NO_EXPERIMENT);
		
		// Active the selected quadrant
		activeQuadrant = experiment.isButton() ?
				buttonsQuadrant : gestureQuadrant;
		activeQuadrant.setQuadrantNo(experiment.quadrantNo);

		int r = experiment.quadrantNo / 2;
		int c = experiment.quadrantNo % 2;
		activeQuadrant.setBoundary(c * width/2, r * height/2, width/2, height/2);
		
		activeQuadrant.nextExperiment(experiment.id);
	}
	
	public GestureDetector.OnGestureListener getTouchListenter() {
		return buttonsQuadrant.getTouchListener();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		
		// clear the canvas
		canvas.drawRect(0, 0, width, height, backgroundPaint);
		
		if (activeQuadrant != null) {
			// draw the active quadrant
			int r = activeQuadrant.getQuadrantNo() / 2;
			int c = activeQuadrant.getQuadrantNo() % 2;
			
			activeQuadrant.draw(canvas,
					c * width/2, r * height/2, width/2, height/2);
		}
	}

	public void setExperimentListener(ExperimentListener listener) {
		buttonsQuadrant.setExperimentListener(listener);
		gestureQuadrant.setExperimentListener(listener);
	}

	public ExperimentQuadrant getActiveQuadrant() {
		return activeQuadrant;
	}
}
