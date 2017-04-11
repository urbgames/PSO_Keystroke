package factory;

import classificator.Classification;
import main.Particle;

public class FactoryParticle {

	private static volatile FactoryParticle instance;
	private int IDGenarator = 0;
	private static int lengthGenes;
	private static Classification classification;

	public static FactoryParticle getInstace() throws Exception {

		if (instance == null) {
			synchronized (FactoryParticle.class) {
				if (instance == null) {
					classification = Classification.getInstance();
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
