package main;

import classificator.Classification;
import classificator.ResultClassification;

public class Fitness {

	public static double getTest(Particle particle) {

		double sum = 0;
		for (int i = 0; i < particle.getSize(); i++) {
			sum += particle.getPosition()[i];
		}

		return sum;

	}

	public static ResultClassification getClassification(Particle particle) throws Exception {
		Classification classification = Classification.getInstance();
		ResultClassification resultClassification = classification.getFitnessClafissation(particle.getBinaryParticle());
		return resultClassification;
	}

}
