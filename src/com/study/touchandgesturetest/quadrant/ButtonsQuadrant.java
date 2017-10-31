package com.study.touchandgesturetest.quadrant;

import java.util.List;

import com.study.touchandgesturetest.ExperimentConstants;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;

public class ButtonsQuadrant extends ExperimentQuadrant
implements ExperimentConstants {

	private static final int BUTTON_MARGIN = 2;
	
	private Paint buttonPaint = new Paint();
	
	private Paint buttonHighlightPaint = new Paint();

	private Paint textPaint = new Paint();
	
	private RectF activeButtonRect;
	
	private PointerCoords touchedPoint;

	private SimpleOnGestureListener onTouchListener;
	
	public ButtonsQuadrant() {
		super();
		
		/*
		 * Init paints used to draw on the canvas
		 */
		buttonPaint.setColor(0xFF948A54);
		buttonHighlightPaint.setColor(0xFFD99694);
		
		textPaint.setColor(0xFF000000);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setTextSize(textPaint.getTextSize() * 2);	
		
		touchedPoint = new PointerCoords();
		
		/*
		 * Active button
		 */
		activeButtonRect = new RectF();
		
		onTouchListener = new SimpleOnGestureListener() {
			@Override
			public boolean onDown(MotionEvent e) {
				// Test - Just bypass the event
//				experimentListener.onExperimentCorrect(quarantNo, activeButtonIndex);

				if (activeExperimentId < 0)
					return false;
				
				// Get touched point
				e.getPointerCoords(0, touchedPoint);
				int x = Math.round(touchedPoint.x);
				int y = Math.round(touchedPoint.y);
				if (!boundary.contains(x, y))
					return false;
				
				// Stop the timeout timer
				endExperiment();
				
				int lastExperimentId = activeExperimentId;
				activeExperimentId = NO_EXPERIMENT;
				
				if (activeButtonRect.contains(x, y)) {
					experimentListener.onExperimentCorrect(quadrantNo, lastExperimentId);
				} else {
					experimentListener.onExperimentError(quadrantNo, lastExperimentId, null);
				}
				
				return true;
			}
		};
	}


	@Override
	public void prepareNextExperiment() {
		
		float buttonWidth  = boundary.width() / BUTTONS_GRID_COLS;
		float buttonHeight = boundary.height() / BUTTONS_GRID_ROWS;

		int r = activeExperimentId / BUTTONS_GRID_COLS;
		int c = activeExperimentId % BUTTONS_GRID_COLS;

		// Compute the boundary of the active button
		activeButtonRect.left = boundary.left + c * buttonWidth;
		activeButtonRect.bottom  = boundary.bottom -  r * buttonHeight;
		activeButtonRect.right = activeButtonRect.left + buttonWidth;
		activeButtonRect.top = activeButtonRect.bottom - buttonHeight;
		activeButtonRect.inset(BUTTON_MARGIN, BUTTON_MARGIN);
		
		Log.d("ButtonsQuadrant", "Button " + activeButtonRect.toShortString());
	}
	
	@Override
	public void draw(Canvas canvas, int left, int top, int width, int height) {
		
		int buttonWidth = width / BUTTONS_GRID_COLS;
		int buttonHeight = height / BUTTONS_GRID_ROWS;
		
		int id = 1;
		for (int r = 0, y = top + height - buttonHeight; 
				r < BUTTONS_GRID_ROWS; r++, y -= buttonHeight) {
			for (int c = 0, x = left; c < BUTTONS_GRID_COLS; 
					c++, x += buttonWidth) {
				drawButton("" + id++, canvas, x, y, 
						buttonWidth, buttonHeight,
						(activeExperimentId >= 0 &&
						 r == activeExperimentId / BUTTONS_GRID_COLS &&
						 c == activeExperimentId % BUTTONS_GRID_COLS));
			}
		}
	}
	
	private void drawButton(String text, Canvas canvas, 
			int x, int y, int width, int height, boolean hightlighted) {
		canvas.drawRect(x + BUTTON_MARGIN, y + BUTTON_MARGIN, 
				x + width - BUTTON_MARGIN, 
				y + height - BUTTON_MARGIN, 
				hightlighted ? buttonHighlightPaint : buttonPaint);
		
		canvas.drawText(text, x + width/2.0f, y + height/2.0f, textPaint);
	}

	@Override
	public void detectMovement(final List<PointF> velocities) {
		// Do-nothing, onDown handles the touch event
	}


	@Override
	public OnGestureListener getTouchListener() {
		return onTouchListener;
	}

}
