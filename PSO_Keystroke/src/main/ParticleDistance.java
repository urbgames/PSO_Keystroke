package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ParticleDistance {

	private Particle particle;
	private int distance;

	public ParticleDistance(Particle particle, int distance) {
		this.particle = particle;
		this.distance = distance;
	}

	public static Particle nearestNeighbors(Particle particle, List<Particle> population, int k) {
		List<ParticleDistance> particleDistances = new ArrayList<ParticleDistance>();
		for (Particle particle2 : population)
			particleDistances.add(new ParticleDistance(particle2, distanceManhattan(particle, particle2)));
		particleDistances.sort(Comparator.comparing(ParticleDistance::getDistance));
		Comparator<ParticleDistance> comparator = (p1, p2) -> Double.compare(p1.particle.getFitness(),
				p2.particle.getFitness());
		return particleDistances.subList(0, k).stream().max(comparator).get().particle;
	}

	private static int distanceManhattan(Particle origem, Particle destino) {
		int countDistance = 0;
		for (int i = 0; i < origem.getBinaryParticle().length; i++)
			if (origem.getBinaryParticle()[i] != destino.getBinaryParticle()[i])
				countDistance++;
		return countDistance;
	}

	public int getDistance() {
		return distance;
	}

}
