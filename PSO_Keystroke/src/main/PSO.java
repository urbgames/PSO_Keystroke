package main;

import java.util.ArrayList;
import java.util.List;

import classificator.Classification;
import excelGenerator.ExcelGenerator;
import factory.FactoryParticle;
import thread.ThreadManager;

public class PSO {

	private List<Particle> population;

	public PSO(int sizePopulation, int maxInteration, int experiment, Classification classifier) throws Exception {

		ExcelGenerator excelGenerator = new ExcelGenerator(
				classifier.getClassifierName() + "_ExperimentoPSO" + experiment);
		ParticleToExcel.createLabelExcel(excelGenerator);
		FactoryParticle factoryParticle = FactoryParticle.getInstace(classifier);

		population = new ArrayList<>();
		for (int i = 0; i < sizePopulation; i++)
			population.add(factoryParticle.factoryParticle());

		for (int i = 0; i < maxInteration; i++) {

			long startTime = System.currentTimeMillis();
			for (int j = 0; j < population.size(); j++)
				Update.updateParticle(population.get(j), classifier);
			Update.updatePopulation(population);
			long totalTime = System.currentTimeMillis() - startTime;
			ParticleToExcel.updateExcelByGeneration(excelGenerator, population, i, totalTime, classifier.getSeed());
		}
		excelGenerator.saveFile();
	}

	public static void main(String[] args) throws Exception {
		new ThreadManager();
	}

}
