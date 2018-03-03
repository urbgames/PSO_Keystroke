package main;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import classificator.Classification;
import classificator.FunctionTest;
import classificator.ResultClassification;

public class Update {

	private final static double VELOCITYCOEFFICIENT = 2;

	public static void updateParticle(Particle particle, Classification classifier) throws Exception {

		double weight = 0.7;
		double pVelocities[] = new double[particle.getSize()];
		double pPositions[] = new double[particle.getSize()];
		for (int i = 0; i < particle.getSize(); i++) {
//			pVelocities[i] = weight * particle.getVelocity()[i]
//					+ VELOCITYCOEFFICIENT * Math.random() * (particle.getpBest()[i] - particle.getPosition()[i])
//					+ VELOCITYCOEFFICIENT * Math.random() * (particle.getgBest()[i] - particle.getPosition()[i]);

//			double position = particle.getPosition()[i] + pVelocities[i];
//			if (position > 2)
//				position = 2;
//			else if (position < -2)
//				position = -2;

//			double S = 1 / (1 + Math.exp(-pVelocities[i]));
//			double position = 0;
//			if (Math.random() >= S)
//				position = 0;
//			else
//				position = 1;

			pPositions[i] = 1;
		}

		particle.setVelocity(pVelocities);
		particle.setPosition(pPositions);

		 ResultClassification resultClassification = Fitness.getClassification(particle, classifier);
//		ResultClassification resultClassification = FunctionTest.getFitness(particle.getBinaryParticle());

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

	public static void updatePopulationlBest(List<Particle> population) {
		for (int i = 0; i < population.size(); i++) {
			final int index = i;
			Particle bestParticle = ParticleDistance.nearestNeighbors(population.get(i), population.stream()
					.filter(p -> p.getID() != population.get(index).getID()).collect(Collectors.toList()), 2);
			if (population.get(i).getFitnessgBest() < bestParticle.getFitness()) {
				population.get(i).setgBest(bestParticle.getPosition());
				population.get(i).setFitnessgBest(bestParticle.getFitness());
				population.get(i).setgBestFAR(bestParticle.getFAR());
				population.get(i).setgBestFRR(bestParticle.getFRR());
			}
		}
	}

}
