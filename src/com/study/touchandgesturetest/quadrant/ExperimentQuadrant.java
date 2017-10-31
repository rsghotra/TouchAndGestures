package com.study.touchandgesturetest.quadrant;

import java.util.List;

import com.study.touchandgesturetest.ExperimentConstants;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;

public abstract class ExperimentQuadrant implements ExperimentConstants {
	
	protected int quadrantNo;
	
	protected RectF boundary;
	
	protected int activeExperimentId;
	
	protected ExperimentListener experimentListener;

	private CountDownTimer timeout;
	
	class TimeoutTimer extends CountDownTimer {

		public TimeoutTimer() {
			super(EXPERIMENT_TIMEOUT, EXPERIMENT_TIMEOUT);
		}

		@Override
		public void onFinish() {
			experimentListener.onExperimentTimeout(quadrantNo, activeExperimentId);
		}

		@Override
		public void onTick(long millisUntilFinished) {}
	}
	
	public ExperimentQuadrant() {
		boundary = new RectF();
		activeExperimentId = NO_EXPERIMENT;
	}

	public void setQuadrantNo(int quadNo) {
		quadrantNo = quadNo;
	}

	public int getQuadrantNo() {
		return quadrantNo;
	}

	public void setBoundary(int x, int y, int w, int h) {
		boundary.set(x, y, x + w, y + h);
		Log.d("ButtonsQuadrant", boundary.toShortString());
	}

	public RectF getBoundary() {
		return boundary;
	}
	
	public void setExperimentListener(ExperimentListener listener) {
		experimentListener = listener;
	}
	
	public void nextExperiment(int experimentId) {
		activeExperimentId = experimentId;
		if (activeExperimentId == ExperimentRepository.NO_EXPERIMENT) {
			endExperiment();
			return;
		}
		
		prepareNextExperiment();
		timeout = new TimeoutTimer().start();
	}
	
	public void endExperiment() {
		if (timeout != null) {
			timeout.cancel();
			timeout = null;
		}
	}

	public abstract void draw(Canvas canvas, 
			int left, int top, int width, int height);
	
	public abstract void prepareNextExperiment();

	public abstract void detectMovement(final List<PointF> velocities);

	public abstract OnGestureListener getTouchListener();

}
