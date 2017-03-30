package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

import classificator.Classification;
import excelGenerator.ExcelGenerator;

public class PSO {

	private List<Particle> population;

	public PSO(int sizePopulation, int sizeParticle, int maxInteration, int experiment) throws Exception {

		ExcelGenerator excelGenerator = new ExcelGenerator("ExperimentoPSO" + experiment);
		excelGenerator.insertCellInfo(0, 0, "Interaction", Cell.CELL_TYPE_STRING);
		excelGenerator.insertCellInfo(0, 1, "gBestFitness(%)", Cell.CELL_TYPE_STRING);
		excelGenerator.insertCellInfo(0, 2, "FAR(%)", Cell.CELL_TYPE_STRING);
		excelGenerator.insertCellInfo(0, 3, "FRR(%)", Cell.CELL_TYPE_STRING);
		excelGenerator.insertCellInfo(0, 4, "Feature", Cell.CELL_TYPE_STRING);
		excelGenerator.insertCellInfo(0, 5, "Enabled Features", Cell.CELL_TYPE_STRING);
		excelGenerator.insertCellInfo(0, 6, "Disabled Features", Cell.CELL_TYPE_STRING);
		excelGenerator.insertCellInfo(0, 7, "Feature Reduction(%)", Cell.CELL_TYPE_STRING);
		excelGenerator.insertCellInfo(0, 8, "Seed", Cell.CELL_TYPE_STRING);
		excelGenerator.insertCellInfo(0, 9, "Time(ms)", Cell.CELL_TYPE_STRING);

		population = new ArrayList<>();
		for (int i = 0; i < sizePopulation; i++)
			population.add(new Particle(sizeParticle));

		for (int i = 0; i < maxInteration; i++) {

			long startTime = System.currentTimeMillis();
			for (int j = 0; j < population.size(); j++)
				Update.updateParticle(population.get(j));
			Update.updatePopulation(population);
			long totalTime = System.currentTimeMillis() - startTime;

			Collections.sort(population, Collections.reverseOrder());
			System.out.println("The best particle is: " + population.get(0).getFitnessgBest() + " in interation " + i);
			excelGenerator.insertCellInfo(i + 1, 0, i, Cell.CELL_TYPE_STRING);
			excelGenerator.insertCellInfo(i + 1, 1, population.get(0).getFitness(), Cell.CELL_TYPE_NUMERIC);
			excelGenerator.insertCellInfo(i + 1, 2, population.get(0).getFAR(), Cell.CELL_TYPE_NUMERIC);
			excelGenerator.insertCellInfo(i + 1, 3, population.get(0).getFRR(), Cell.CELL_TYPE_NUMERIC);
			excelGenerator.insertCellInfo(i + 1, 4, population.get(0).toStringBinary(), Cell.CELL_TYPE_STRING);
			excelGenerator.insertCellInfo(i + 1, 5, population.get(0).getEnableFeatures(), Cell.CELL_TYPE_NUMERIC);
			excelGenerator.insertCellInfo(i + 1, 6, population.get(0).getDisabledFeatures(), Cell.CELL_TYPE_NUMERIC);
			excelGenerator.insertCellInfo(i + 1, 7, population.get(0).getFeatureReduction(), Cell.CELL_TYPE_NUMERIC);
			excelGenerator.insertCellInfo(i + 1, 8, Classification.getInstance().getSeed(), Cell.CELL_TYPE_NUMERIC);
			excelGenerator.insertCellInfo(i + 1, 9, totalTime, Cell.CELL_TYPE_NUMERIC);

		}

		excelGenerator.saveFile();
	}

	public static void main(String[] args) throws Exception {

		for (int i = 0; i < 10; i++) {
			new PSO(100, 71, 100, i);
			Classification.getInstance().changeSeed();
		}
	}

}
