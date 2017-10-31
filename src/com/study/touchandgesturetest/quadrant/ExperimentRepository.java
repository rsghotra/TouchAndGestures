package com.study.touchandgesturetest.quadrant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.study.touchandgesturetest.ExperimentConstants;

public class ExperimentRepository implements ExperimentConstants {

	public class Experiment {
		public Experiment(int id, int quadrantNo) {
			this.id = id;
			this.quadrantNo = quadrantNo;
		}
		public boolean isButton() {
			return id < NUM_BUTTONS;
		}
		public int id;
		public int quadrantNo;
	}
	
	private List<Experiment> remainingItems;
	
	private Random random;
	
	private Experiment lastExperiment;
	
	public ExperimentRepository() {
		random = new Random(System.currentTimeMillis());
		
		remainingItems = new ArrayList<ExperimentRepository.Experiment>();
		for (int i = 0; i < TOTAL_EXPERIMENTS; i++) {
			remainingItems.add(new Experiment(i, i % 4));
		}
		
		// Shuffle the items several times
		Collections.shuffle(remainingItems, random);
		Collections.shuffle(remainingItems, random);
		Collections.shuffle(remainingItems, random);
	}

	public Experiment nextExperiment() {
		// Pick next button/sign to be experimented
		int location = random.nextInt(remainingItems.size());
		Experiment experiment = remainingItems.get(location);
		while (lastExperiment != null &&
				experiment.isButton() == lastExperiment.isButton()) {
			location = random.nextInt(remainingItems.size());
			experiment = remainingItems.get(location);
		}
		
		lastExperiment = experiment;
		return remainingItems.remove(location);
	}
	
	public boolean hashNextExperiment() {
		return remainingItems.size() > 0;
	}
}
