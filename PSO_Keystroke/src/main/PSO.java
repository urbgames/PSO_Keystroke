package main;

import java.util.ArrayList;
import java.util.List;

import classificator.Classification;
import excelGenerator.ExcelGenerator;
import factory.FactoryParticle;

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

		int repetition = args.length == 3 ? Integer.parseInt(args[0]) : 30;
		int sizePopulation = args.length == 3 ? Integer.parseInt(args[1]) : 100;
		int maxInteration = args.length == 3 ? Integer.parseInt(args[2]) : 100;

		if(args.length>0){
			System.out.println(args[0]);
			System.out.println(args[1]);
			System.out.println(args[2]);
		}
		
		Thread thread = new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < repetition; i++) {
					try {
						new PSO(sizePopulation, maxInteration, i, new Classification(Classification.BAYESNET));
						System.out.println(Classification.BAYESNET+" Fim da interação " + i );
					} catch (Exception e) {
					}
				}
			}
		});
		thread.start();

		Thread thread2 = new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < repetition; i++) {
					try {
						Classification classification = new Classification(Classification.J48);
						new PSO(sizePopulation, maxInteration, i, classification);
						System.out.println(Classification.J48+" Fim da interação " + i );
					} catch (Exception e) {
					}
				}
			}
		});
		thread2.start();

		Thread thread3 = new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < repetition; i++) {
					try {
						Classification classification = new Classification(Classification.KNN);
						new PSO(sizePopulation, maxInteration, i, classification);
						System.out.println(Classification.KNN+" Fim da interação " + i );
					} catch (Exception e) {
					}
				}
			}
		});
		thread3.start();

		Thread thread4 = new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < repetition; i++) {
					try {
						Classification classification = new Classification(Classification.MLP);
						new PSO(sizePopulation, maxInteration, i, classification);
						System.out.println(Classification.MLP+" Fim da interação " + i );
					} catch (Exception e) {
					}
				}
			}
		});
		thread4.start();

		Thread thread5 = new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < repetition; i++) {
					try {
						new PSO(sizePopulation, maxInteration, i, new Classification(Classification.NAIVEBAYES));
						System.out.println(Classification.NAIVEBAYES+" Fim da interação " + i );
					} catch (Exception e) {
					}
				}
			}
		});
		thread5.start();

		Thread thread6 = new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < repetition; i++) {
					try {
						Classification classification = new Classification(Classification.RANDOMFOREST);
						new PSO(sizePopulation, maxInteration, i, classification);
						System.out.println(Classification.RANDOMFOREST+" Fim da interação " + i );
					} catch (Exception e) {
					}
				}
			}
		});
		thread6.start();

		Thread thread7 = new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < repetition; i++) {
					try {
						Classification classification = new Classification(Classification.SVM);
						new PSO(sizePopulation, maxInteration, i, classification);
						System.out.println(Classification.SVM+" Fim da interação " + i );
					} catch (Exception e) {
					}
				}
			}
		});
		thread7.start();

	}

}
