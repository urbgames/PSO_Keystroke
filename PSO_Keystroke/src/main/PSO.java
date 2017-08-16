package main;

import java.util.ArrayList;
import java.util.List;

import classificator.Classification;
import excelGenerator.ExcelGenerator;
import excelGenerator.ParticleToExcel;
import factory.FactoryParticle;
import systemStatusControl.StatusControl;
import systemStatusControl.StatusPSO;
import thread.ThreadManager;

public class PSO {

	private List<Particle> population;

	public PSO(int sizePopulation, int maxInteration, int experiment, Classification classifier, int seed,
			StatusPSO... statusPSO) {
		FactoryParticle factoryParticle = null;
		try {
			factoryParticle = FactoryParticle.getInstace(classifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int currentInteration = 0;
		if (statusPSO.length > 0) {
			population = statusPSO[0].getPopulacao();
			seed = statusPSO[0].getSeed();
			currentInteration = statusPSO[0].getCurrentInteretor() + 1;
		} else {
			population = new ArrayList<>();
			for (int i = 0; i < sizePopulation; i++)
				population.add(factoryParticle.factoryParticle());
		}

		ExcelGenerator excelGenerator = new ExcelGenerator(
				classifier.getClassifierName() + "_ExperimentoPSO" + experiment);
		ParticleToExcel.createLabelExcel(excelGenerator);

		for (; currentInteration < maxInteration; currentInteration++) {
			long startTime = System.currentTimeMillis();
			for (int j = 0; j < population.size(); j++)
				try {
					Update.updateParticle(population.get(j), classifier);
				} catch (Exception e) {
					e.printStackTrace();
				}
			Update.updatePopulation(population);
			long totalTime = System.currentTimeMillis() - startTime;
			ParticleToExcel.updateExcelByGeneration(excelGenerator, population, currentInteration, totalTime,
					classifier.getSeed());
			try {
				StatusControl.insertOrUpdate(new StatusPSO(population, currentInteration, maxInteration, experiment,
						sizePopulation, seed, classifier.getClassifierName()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// excelGenerator.saveFile();
	}

	public static void main(String[] args) {
		System.out.println("PSO");
		new ThreadManager();
	}

}
