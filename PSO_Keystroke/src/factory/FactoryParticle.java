package factory;

import classificator.Classification;
import main.Particle;

public class FactoryParticle {

	private static volatile FactoryParticle instance;
	private static int lengthGenes;

	public static FactoryParticle getInstace(Classification classification) throws Exception {

		if (instance == null) {
			synchronized (FactoryParticle.class) {
				if (instance == null) {
					lengthGenes = classification.getLength();
					instance = new FactoryParticle();
				}
			}
		}
		return instance;
	}

	public Particle factoryParticle() {
		return new Particle(lengthGenes);
	}

}
