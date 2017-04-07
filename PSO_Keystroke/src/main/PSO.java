package main;

import java.util.ArrayList;
import java.util.List;

import classificator.Classification;
import excelGenerator.ExcelGenerator;
import factory.FactoryParticle;

public class PSO {

	private List<Particle> population;

	public PSO(int sizePopulation, int maxInteration, int experiment) throws Exception {

		ExcelGenerator excelGenerator = new ExcelGenerator("ExperimentoPSO" + experiment);
		ParticleToExcel.createLabelExcel(excelGenerator);
		FactoryParticle factoryParticle = FactoryParticle.getInstace();

		population = new ArrayList<>();
		for (int i = 0; i < sizePopulation; i++)
			population.add(factoryParticle.factoryParticle());

		for (int i = 0; i < maxInteration; i++) {

			long startTime = System.currentTimeMillis();
			for (int j = 0; j < population.size(); j++)
				Update.updateParticle(population.get(j));
			Update.updatePopulation(population);
			long totalTime = System.currentTimeMillis() - startTime;

			ParticleToExcel.updateExcelByGeneration(excelGenerator, population, i, totalTime);

		}

		excelGenerator.saveFile();
	}

	public static void main(String[] args) throws Exception {

		for (int i = 0; i < 2; i++) {
			new PSO(10, 10, i);
			Classification.getInstance().changeSeed();
		}
	}

}
