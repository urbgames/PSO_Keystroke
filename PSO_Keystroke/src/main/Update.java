package main;

import java.util.Collections;
import java.util.List;

import classificator.Classification;
import classificator.ResultClassification;

public class Update {

	private final static double VELOCITYCOEFFICIENT = 2;

	public static void updateParticle(Particle particle, Classification classifier) throws Exception {

		double weight = 0.7;
		double pVelocities[] = new double[particle.getSize()];
		double pPositions[] = new double[particle.getSize()];
		for (int i = 0; i < particle.getSize(); i++) {
			pVelocities[i] = weight * particle.getVelocity()[i]
					+ VELOCITYCOEFFICIENT * Math.random() * (particle.getpBest()[i] - particle.getPosition()[i])
					+ VELOCITYCOEFFICIENT * Math.random() * (particle.getgBest()[i] - particle.getPosition()[i]);
			double position = particle.getPosition()[i] + pVelocities[i];
			System.out.println(position);
			if (position > 2)
				position = 2;
			else if (position < -2)
				position = -2;
			pPositions[i] = position;
		}

		particle.setVelocity(pVelocities);
		particle.setPosition(pPositions);

		ResultClassification resultClassification = Fitness.getClassification(particle, classifier);
		particle.setFitness(resultClassification.getPctCorrect());
		particle.setFAR(resultClassification.getFAR());
		particle.setFRR(resultClassification.getFRR());

		if (particle.getFitnesspBest() < particle.getFitness()) {
			particle.setpBest(pPositions);
			particle.setFitnesspBest(particle.getFitness());
			particle.setFAR(particle.getFAR());
			particle.setFRR(particle.getFRR());
		}
	}

	public static void updatePopulation(List<Particle> population) {
		Collections.sort(population, Collections.reverseOrder());
		if (population.get(0).getFitnessgBest() < population.get(0).getFitness()
				|| population.get(0).getFitnessgBest() == 0)
			for (int i = 0; i < population.size(); i++) {
				population.get(i).setgBest(population.get(0).getPosition());
				population.get(i).setFitnessgBest(population.get(0).getFitness());
				population.get(i).setgBestFAR(population.get(0).getFAR());
				population.get(i).setgBestFRR(population.get(0).getFRR());
			}
	}

}
