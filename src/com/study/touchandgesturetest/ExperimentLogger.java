package com.study.touchandgesturetest;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import android.os.Environment;


public class ExperimentLogger implements ExperimentConstants {

	private int userNo;
	
	private String usingType;
	
	private long experimentStartTime;
	
	private class Result {
		
		public Result(int quadrantNo, int result, long elapsedTime) {
			this.quadrantNo = quadrantNo;
			this.result = result;
			this.elapsedTime = elapsedTime;
		}
		
		int quadrantNo;
		int result;		  // SUCCESS, ERROR, or TIMEOUT
		long elapsedTime; // miliseconds
	}
	
	private Result[] experimentResults;
	
	public ExperimentLogger(int userNo, String usingType) {
		this.userNo = userNo;
		this.usingType = usingType;
		
		experimentResults = new Result[TOTAL_EXPERIMENTS];
	}

	public void startExperiment() {
		experimentStartTime = System.currentTimeMillis();
	}

	public void endExperiment(int quadrantNo, int experimentId, 
			int result, String reason) {
		
		experimentResults[experimentId] = new Result(
				quadrantNo,
				result,
				System.currentTimeMillis() - experimentStartTime);
	}
	
	public String save() throws IOException {
		
		File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		if (!dir.exists() && !dir.mkdirs())
			throw new IOException(dir.getPath() + ": Download directory is not accessible");

		String fileName = RESULT_FILE_PREFIX + 
				System.currentTimeMillis() + ".txt";
		
		File file = new File(dir, fileName);
		PrintWriter printer = new PrintWriter(file);
		
		printer.println("UserNo: " + userNo);
		printer.println("Type: " + usingType);
		
		long totalTime = 0;
		long[] timeSpentOnButtons = new long[4];
		long[] timeSpentOnGestures = new long[4];
		int[] buttonsPerQuadrant = new int[4];
		int[] gesturesPerQuadrant = new int[4];
		for (int i = 0; i < experimentResults.length; i++) {
			Result r = experimentResults[i];
			if (r != null) {
				if (i < NUM_BUTTONS) {
					buttonsPerQuadrant[r.quadrantNo]++;
					timeSpentOnButtons[r.quadrantNo] += r.elapsedTime;
				} else {
					gesturesPerQuadrant[r.quadrantNo]++;
					timeSpentOnGestures[r.quadrantNo] += r.elapsedTime;
				}
				totalTime += r.elapsedTime;
			}
		}
		
		printer.printf("Total Time Elapsed: %.2f secs\n", totalTime / 1000f);
		
		printer.println();
		for (int i = 0; i < TOTAL_EXPERIMENTS; i++) {
			Result r = experimentResults[i];
			printer.printf("%s %d (Quadrant %d): %.2f secs\n",
					i < NUM_BUTTONS ? "Button" : "Sign",
					i < NUM_BUTTONS ? (i+1) : (i - NUM_BUTTONS + 1),
					r != null ? (r.quadrantNo + 1) : 0,
					(r != null ? r.elapsedTime : 0) / 1000f);
			
		}
		
		printer.println();
		for (int i = 0; i < 4; i++) {
			printer.printf("Avg_time_for_buttons_shown_in_quadrant%d = %.2f secs\n",
					i + 1, timeSpentOnButtons[i] / buttonsPerQuadrant[i] / 1000f);
		}
		for (int i = 0; i < 4; i++) {
			printer.printf("Avg_time_for_gestures_shown_in_quadrant%d = %.2f secs\n",
					i + 1, timeSpentOnGestures[i] / gesturesPerQuadrant[i] / 1000f);
		}

		// Error table
		printer.println();
		printer.println("Error Table:");
		printer.println("Button/Sign Quadrant Error/Timeout");
		printer.println("----------- -------- -------------");
		for (int i = 0; i < experimentResults.length; i++) {
			Result r = experimentResults[i];
			if (r == null || r.result != SUCCESS) {
				printer.printf(
						"%-11s %-8d %s\n",
						(i < NUM_BUTTONS ? "Button " : "Sign ") + 
							(i < NUM_BUTTONS ? (i + 1) : (i - NUM_BUTTONS + 1)),
						r == null ? "-1" : (4 - r.quadrantNo), 
						(r != null && r.result == ERROR) ? "Error" : "Timeout");
			}
		}
		
		printer.close();
		
		return file.getPath();
	}
	
	public String save2() throws IOException {
		
		File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		if (!dir.exists() && !dir.mkdirs())
			throw new IOException(dir.getPath() + ": Download directory is not accessible");

		String fileName = RESULT_FILE_PREFIX + 
				System.currentTimeMillis() + ".txt";
		
		File file = new File(dir, fileName);
		PrintWriter printer = new PrintWriter(file);
		
		printer.println("UserNo: " + userNo);
		printer.println("Type: " + usingType);
		
		long totalTime = 0;
		long[] timePerExperiment = new long[TOTAL_EXPERIMENTS];
		long[] timePerQuadrant = new long[4];
		for (int i = 0; i < experimentResults.length; i++) {
			Result r = experimentResults[i];
			if (r != null) {
				timePerExperiment[i] += r.elapsedTime;
				timePerQuadrant[r.quadrantNo] += r.elapsedTime;
				totalTime += r.elapsedTime;
			}
		}
		
		printer.printf("Total Time Elapsed: %.2f secs\n", totalTime / 1000f);
		printer.printf("Time Elapsed in Quadrant 1: %.2f secs\n", timePerQuadrant[3] / 1000f);	
		printer.printf("Time Elapsed in Quadrant 2: %.2f secs\n", timePerQuadrant[2] / 1000f);		
		printer.printf("Time Elapsed in Quadrant 3: %.2f secs\n", timePerQuadrant[1] / 1000f);		
		printer.printf("Time Elapsed in Quadrant 4: %.2f secs\n", timePerQuadrant[0] / 1000f);
		
		for (int i = 0; i < TOTAL_EXPERIMENTS; i++) {
			printer.printf("Time Per %s %d: %.2f secs\n",
					i < NUM_BUTTONS ? "Button" : "Sign",
					i < NUM_BUTTONS ? (i+1) : (i - NUM_BUTTONS + 1),
					timePerExperiment[i] / 1000f);
		}
		
		// Error table
		printer.println("Error Table:");
		printer.println("Button/Sign Quadrant Error/Timeout");
		printer.println("----------- -------- -------------");
		for (int i = 0; i < experimentResults.length; i++) {
			Result r = experimentResults[i];
			if (r == null || r.result != SUCCESS) {
				printer.printf(
						"%-11s %-8d %s\n",
						(i < NUM_BUTTONS ? "Button " : "Sign ") + 
							(i < NUM_BUTTONS ? (i + 1) : (i - NUM_BUTTONS + 1)),
						r == null ? "-1" : (4 - r.quadrantNo), 
						(r != null && r.result == ERROR) ? "Error" : "Timeout");
			}
		}
		
		printer.close();
		
		return file.getPath();
	}
}
