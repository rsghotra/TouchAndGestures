package com.study.touchandgesturetest.util;

public class MathUtils {

	public static boolean nearlyZero(float x, float epsilon) {
		return x >= -epsilon && x <= epsilon;
	}
	
	public static float length(float x, float y) {
		return (float) Math.sqrt(x * x + y * y);
	}
}
