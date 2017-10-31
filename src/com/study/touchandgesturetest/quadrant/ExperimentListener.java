package com.study.touchandgesturetest.quadrant;

public interface ExperimentListener {

	public void onExperimentCorrect(int quadrantNo, int experimentId);
	
	public void onExperimentError(int quadrantNo, int experimentId, String reason);
	
	public void onExperimentTimeout(int quadrantNo, int experimentId);
}
