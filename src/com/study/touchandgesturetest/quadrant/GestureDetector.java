package com.study.touchandgesturetest.quadrant;

import java.util.ArrayList;
import java.util.List;

import android.graphics.PointF;

public class GestureDetector {
	
	public static final int MIN_NUM_POINTS = 6;

	private static final int LEFT = 1;
	private static final int RIGHT = 2;
	private static final int TOP = 3;
	private static final int BOTTOM = 4;
	
	private static final int[][] SIGNS = {
		{LEFT, TOP}, // up then right
		{RIGHT, TOP}, // up then left
		{BOTTOM, LEFT}, // left then up
		{BOTTOM, RIGHT}, // right then up
		{TOP, RIGHT}, // right then down
		{TOP, LEFT}, // left then down
		{LEFT, BOTTOM}, // down then right
		{RIGHT, BOTTOM}, // down then left
	};
	
	private List<PointF> points;
	
	private int splitIndex;
	
	public GestureDetector(List<PointF> pts) throws GestureDetectorException {
		makeBoundary(pts);
		if (points.size() < MIN_NUM_POINTS)
			throw new GestureDetectorException("Not enough points");
		
		// Remove the first and last points
		points.remove(0);
		points.remove(points.size() - 1);
		
		splitSegments();
	}
	
	public void match(int signId) throws Exception {
		int[] sign = SIGNS[signId];
		
		for (int i = 1; i < points.size() - 1; i++) {
			PointF p = points.get(i);
			int segmentType = i < splitIndex ? sign[0] : sign[1];
			
			if (belongToSegment(p, 
					segmentType == LEFT || segmentType == RIGHT,
					segmentType == TOP || segmentType == BOTTOM) != segmentType)
				throw new Exception("Miss match on " +
						(i < splitIndex ? "first" : "second") + " segment");
		}
	}
	
	private void makeBoundary(List<PointF> pts) {
		points = new ArrayList<PointF>(pts.size());
		
		float minX = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;
		float minY = Float.MAX_VALUE;
		float maxY = Float.MIN_VALUE;
		for (PointF p: pts) {
			if (p.x < minX) minX = p.x;
			if (p.y < minY) minY = p.y;
			if (p.x > maxX) maxX = p.x;
			if (p.y > maxY) maxY = p.y;
		}
		
		float lenX = maxX - minX;
		float lenY = maxY - minY;
		for (PointF p: pts) {
			points.add(new PointF((p.x - minX) / lenX, (p.y - minY) / lenY));
		}
	}
	
	private void splitSegments() {
		
		int segment1Type = -1;
		for (splitIndex = 1; splitIndex < points.size() - 1; splitIndex++) {
			int type = belongToSegment(points.get(splitIndex), true, true);
			
			if (segment1Type < 0) {
				segment1Type = type;
			} else if (type != segment1Type) {
				break;
			}
			
			splitIndex++;
		}
	}
	
	private int belongToSegment(PointF p, boolean vertTest, boolean horzTest) {
		int type = 0;
		float min = Float.MAX_VALUE;
		
		if (vertTest) {
			if (Math.abs(p.x) < min) {
				type = LEFT;
				min = Math.abs(p.x);
			}
			if (Math.abs(p.x - 1) < min) {
				type = RIGHT;
				min = Math.abs(p.x - 1);
			}
		}
		
		if (horzTest) {
			if (Math.abs(p.y) < min) {
				type = TOP;
				min = Math.abs(p.y);
			}
			if (Math.abs(p.y - 1) < min) {
				type = BOTTOM;
				min = Math.abs(p.y - 1);
			}
		}
		
		return type;
	}
}
