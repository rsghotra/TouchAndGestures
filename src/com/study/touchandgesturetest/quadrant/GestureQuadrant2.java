package com.study.touchandgesturetest.quadrant;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;

import com.study.touchandgesturetest.ExperimentConstants;
import com.study.touchandgesturetest.util.MathUtils;

public class GestureQuadrant2 extends ExperimentQuadrant 
implements ExperimentConstants {
	
	private Paint signPaint = new Paint();
	
	private Paint highlightPaint = new Paint();

	public GestureQuadrant2() {
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
	public void detectMovement(final List<PointF> velocities) {
		if (activeExperimentId < 0)
			return;
		
		// Stop the timer
		endExperiment();
		
		List<PointF> move1 = null;
		List<PointF> move2 = null;
//		int offset = velocities.size() / 10;
//		for (int i = offset; i < velocities.size() - offset; i++) {
//			PointF p = velocities.get(i);
//			if (MathUtils.nearlyZero(p.x, EPSILON) && 
//					MathUtils.nearlyZero(p.y, EPSILON)) {
//				move1 = velocities.subList(0, i);
//				move2 = velocities.subList(i + 1, velocities.size());
//				break;
//			}
//		}
		float min = Float.MAX_VALUE;
		int minIndex = 0;
		for (int i = 2; i < velocities.size() - 2; i++) {
			PointF p = velocities.get(i);
			float len = MathUtils.length(p.x, p.y);
			if (len < min) {
				min = len;
				minIndex = i;
			}
		}
		move1 = velocities.subList(0, minIndex);
		move2 = velocities.subList(minIndex + 1, velocities.size());
		Log.d("GestureQuadrant", "Move 1: " + move1.size() + ", Move 2: " +
				move2.size());
		
		if (move1.size() < 2 || move2.size() < 2) {
			experimentListener.onExperimentError(quadrantNo, activeExperimentId, null);
			return;
		}

		int[][] anchorPoints = SIGNS[activeExperimentId - NUM_BUTTONS];
		
		if (!testMove(move1, anchorPoints[0], anchorPoints[1]) ||
				!testMove(move2, anchorPoints[1], anchorPoints[2])) {
			experimentListener.onExperimentError(quadrantNo, activeExperimentId, null);
			return;
		}
		
		experimentListener.onExperimentCorrect(quadrantNo, activeExperimentId);
	}

	private boolean testMove(List<PointF> move, int[] anchorPoint1, int[] anchorPoint2) {
		
		float vx = anchorPoint2[0] - anchorPoint1[0];
		float vy = anchorPoint2[1] - anchorPoint1[1];
		
		float averageVx = 0;
		float averageVy = 0;
		for (PointF v: move) {
			averageVx += v.x;
			averageVy += v.y;
		}
		
		// Normalize
		float len = MathUtils.length(averageVx, averageVy);
		averageVx /= len;
		averageVy /= len;
		
		Log.d("GestureQuadrant", "Expected Movement (" + vx + "," + vy +
				"), average (" + averageVx + "," + averageVy + ")");
		
		return (Math.abs(averageVx - vx) < ACCEPTANT_ERROR &&
				Math.abs(averageVy - vy) < ACCEPTANT_ERROR);
	}

	@Override
	public OnGestureListener getTouchListener() {
		return null;
	}
}
