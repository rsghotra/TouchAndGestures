package com.study.touchandgesturetest;

public interface ExperimentConstants {

	/** Prefix for result files which are saved in the public Downloads folder */
	public static final String RESULT_FILE_PREFIX = "gesture_test_";

	/** Experiment time out per sign/button */
	public static final long EXPERIMENT_TIMEOUT = 20 * 1000; // 5 seconds

	/**
	 * Constant to detect gesture/sign:
	 * Vary this value from 0 - 1:
	 * 	0 - most precise (absolutely correct)
	 * 	1 - least precise (accepting all gesture)
	 */
	public static final float ACCEPTANT_ERROR = 0.55f;
	
	public static final int VELOCITY_UNITS = 10; // 1/10 seconds
	public static final float VELOCITY_MAX = 1.0f;
	public static final float EPSILON = 0.01f; // to detect gesture corner
	
	public static final int NUM_BUTTONS = 8;
	public static final int NUM_SIGNS = 8;
	public static final int TOTAL_EXPERIMENTS = NUM_BUTTONS + NUM_SIGNS;

	public static final int NO_EXPERIMENT = -1;

	public static final int BUTTONS_GRID_COLS = 4;
	public static final int BUTTONS_GRID_ROWS = 2;
	
	public final static int SUCCESS = 1;
	public final static int ERROR   = 2;
	public final static int TIMEOUT = 3;
	
	public static final int EAST = 0;
	public static final int WEST = 1;
	public static final int NORTH = 2;
	public static final int SOUTH = 3;
//	public static final int SOUTH_EAST = 4;
//	public static final int SOUTH_WEST = 5;
//	public static final int NORTH_EAST = 6;
//	public static final int NORTH_WEST = 7;
	
	public static final int[][][] ARROWS = {
		{ {-1,-1}, {-1, 1} },	// East
		{ { 1,-1}, { 1, 1} },	// West
		{ {-1, 1}, { 1, 1} },	// North
		{ {-1,-1}, { 1,-1} },	// South
//		{ { 0,-1}, {-1, 0} },	// South-East
//		{ { 0,-1}, { 1, 0} },	// South-West
//		{ { 0,-1}, {-1, 0} },	// North-East
//		{ { 1, 0}, { 0, 1} },	// North-West
	};
	
	public static final int[][][] SIGNS = {
		{ {0,1}, {0,0}, {1,0} }, // up then right
		{ {1,1}, {1,0}, {0,0} }, // up then left
		{ {1,1}, {0,1}, {0,0} }, // left then up
		{ {0,1}, {1,1}, {1,0} }, // right then up
		{ {0,0}, {1,0}, {1,1} }, // right then down
		{ {1,0}, {0,0}, {0,1} }, // left then down
		{ {0,0}, {0,1}, {1,1} }, // down then right
		{ {1,0}, {1,1}, {0,1} }, // down then left
//		{ {1,0}, {0,0}, {1,1} }, // right then diagonal
//		{ {0,0}, {1,0}, {0,1} }, // left then diagonal
//		{ {1,0}, {1,1}, {0,0} }, // down then diagonal
//		{ {0,1}, {0,0}, {1,1} }, // up then diagonal
	};
	
	public static final int[] SIGNS_ARROW = {
		EAST,
		WEST,
		NORTH,
		NORTH,
		SOUTH,
		SOUTH,
		EAST,
		WEST,
//		SOUTH_EAST,
//		SOUTH_WEST,
//		NORTH_WEST,
//		NORTH_EAST
	};

}
