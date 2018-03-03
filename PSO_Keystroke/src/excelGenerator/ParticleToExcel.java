package excelGenerator;

import java.util.Comparator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

import main.Particle;
import main.ParticleDistance;

public class ParticleToExcel {
	
	public static void createLabelExcel(ExcelGenerator excelGenerator){
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
	}


	public static void updateExcelByGeneration(ExcelGenerator excelGenerator,List<Particle> population, int interation, long totalTime, int seed){
		
		population.sort(Comparator.comparing(Particle::getFitnessgBest));
		
		excelGenerator.insertCellInfo(interation + 1, 0, interation, Cell.CELL_TYPE_STRING);
//		excelGenerator.insertCellInfo(interation + 1, 1, population.get(population.size()-1).getFitnessgBest(), Cell.CELL_TYPE_NUMERIC);
//		excelGenerator.insertCellInfo(interation + 1, 2, population.get(0).getgBestFAR(), Cell.CELL_TYPE_NUMERIC);
//		excelGenerator.insertCellInfo(interation + 1, 3, population.get(0).getgBestFRR(), Cell.CELL_TYPE_NUMERIC);
//		
		excelGenerator.insertCellInfo(interation + 1, 1, population.get(0).getFitness(), Cell.CELL_TYPE_NUMERIC);
		excelGenerator.insertCellInfo(interation + 1, 2, population.get(0).getFAR(), Cell.CELL_TYPE_NUMERIC);
		excelGenerator.insertCellInfo(interation + 1, 3, population.get(0).getFRR(), Cell.CELL_TYPE_NUMERIC);
		
		excelGenerator.insertCellInfo(interation + 1, 4, population.get(0).toStringBinarygBest(), Cell.CELL_TYPE_STRING);
		excelGenerator.insertCellInfo(interation + 1, 5, population.get(0).getEnableFeaturesgBest(), Cell.CELL_TYPE_NUMERIC);
		excelGenerator.insertCellInfo(interation + 1, 6, population.get(0).getDisabledFeaturesgBest(), Cell.CELL_TYPE_NUMERIC);
		excelGenerator.insertCellInfo(interation + 1, 7, population.get(0).getFeatureReductiongBest(), Cell.CELL_TYPE_NUMERIC);
		excelGenerator.insertCellInfo(interation + 1, 8, seed, Cell.CELL_TYPE_NUMERIC);
		excelGenerator.insertCellInfo(interation + 1, 9, totalTime, Cell.CELL_TYPE_NUMERIC);

	}
		
}
