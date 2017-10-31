package com.study.touchandgesturetest.quadrant;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.view.GestureDetector.OnGestureListener;

import com.study.touchandgesturetest.ExperimentConstants;

public class GestureQuadrant extends ExperimentQuadrant 
implements ExperimentConstants {
	
	private Paint signPaint = new Paint();
	
	private Paint highlightPaint = new Paint();

	public GestureQuadrant() {
		super();
		
		/*
		 * Init paints used to draw on the canvas
		 */
		signPaint.setColor(0xFFFFFFFF);
		signPaint.setStyle(Style.STROKE);
		signPaint.setStrokeWidth(4);
		signPaint.setStrokeCap(Cap.ROUND);
		
		highlightPaint.setColor(0xFFD99694);
	}

	@Override
	public void prepareNextExperiment() {
	}

	@Override
	public void draw(Canvas canvas, int left, int top, int width, int height) {
		if (activeExperimentId < 0)
			return;
		
		// highlight the quadrant
		canvas.drawRect(boundary, highlightPaint);
		
		final float signW = width/3.0f;
		final float signH = height/3.0f;
		final float arrowW = width/20.0f;
		final float arrowH = width/20.0f;
		
		int signIndex = activeExperimentId - NUM_BUTTONS;
		int[][] sign = SIGNS[signIndex];
		canvas.drawLine(
				left + signW + sign[0][0] * signW,
				top  + signH + sign[0][1] * signH,
				left + signW + sign[1][0] * signW,
				top  + signH + sign[1][1] * signH,
				signPaint);
		canvas.drawLine(
				left + signW + sign[1][0] * signW,
				top  + signH + sign[1][1] * signH,
				left + signW + sign[2][0] * signW,
				top  + signH + sign[2][1] * signH,
				signPaint);
		drawArrow(canvas, 
				left + signW + sign[2][0] * signW,
				top  + signH + sign[2][1] * signH, 
				arrowW, arrowH,
				SIGNS_ARROW[signIndex]);
	}
	
	private void drawArrow(Canvas canvas, float x, float y,
			float arrowW, float arrowH, int direction) {
		
		canvas.drawLine(
				x, y, 
				x + arrowW * ARROWS[direction][0][0], 
				y + arrowH * ARROWS[direction][0][1],
				signPaint);
		canvas.drawLine(
				x, y, 
				x + arrowW * ARROWS[direction][1][0], 
				y + arrowH * ARROWS[direction][1][1],
				signPaint);
	}

	@Override
	public void detectMovement(final List<PointF> points) {
		if (activeExperimentId < 0)
			return;
		
		// Stop the timer
		endExperiment();
		
		try {
			GestureDetector detector = new GestureDetector(points);
			detector.match(activeExperimentId - NUM_BUTTONS);
			experimentListener.onExperimentCorrect(quadrantNo, activeExperimentId);
			
		} catch (Exception e) {
			experimentListener.onExperimentError(quadrantNo, activeExperimentId, e.getMessage());
		}
	}

	@Override
	public OnGestureListener getTouchListener() {
		return null;
	}
}
