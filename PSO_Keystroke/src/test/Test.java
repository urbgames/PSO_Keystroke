package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import classificator.Classification;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

public class Test {

	private Instances data;
	private Classifier classifier;

	public Test() throws Exception {
		String base3 = "keystroke_71features.arff";
		data = new DataSource(base3).getDataSet();
		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);
		
		classifier = new BayesNet();
	}

	public void testClassificationPercent() throws Exception {

		// VALIDAÇÃO CRUZADA
		/*
		 * Evaluation eval = new Evaluation(data); try {
		 * eval.crossValidateModel(classifier, data, 10, new Random(seed)); }
		 * catch (Exception e) { eval.crossValidateModel(classifier, data, 10,
		 * new Random(seed)); }
		 */

		data.randomize(new Random(2));
		
		RemovePercentage percentageData = new RemovePercentage();
		percentageData.setInputFormat(data);
		percentageData.setOptions(Utils.splitOptions("-P 90"));
		Instances dataTest = Filter.useFilter(data, percentageData);

		percentageData.setOptions(Utils.splitOptions("-V -P 90"));
		Instances dataTrain = Filter.useFilter(data, percentageData);

		classifier.buildClassifier(dataTrain);
		Evaluation eval = new Evaluation(dataTrain);
		eval.evaluateModel(classifier, dataTest);

		System.out.println(eval.pctCorrect());
	}

	public void crossValidation() throws Exception{


		// BayesNet classifier = new BayesNet();
		//
		// Object object = new Object[] {};
		//
		// if (data.classIndex() == -1)
		// data.setClassIndex(data.numAttributes() - 1);
		// // VALIDAÇÃO CRUZADA
		// Evaluation eval = new Evaluation(data);
		// try {
		// eval.crossValidateModel(classifier, data, 10, new Random(1));
		// } catch (Exception e) {
		// eval.crossValidateModel(classifier, data, 10, new Random(1));
		// }

		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);

		BayesNet classifier = new BayesNet();
		Evaluation eval = new Evaluation(data);

		// Make a copy of the data we can reorder
		Instances dataTemp = new Instances(data);

		System.out.println(dataTemp.numInstances());

		int numFolds = 10;
		dataTemp.randomize(new Random(1));
		if (dataTemp.classAttribute().isNominal()) {
			dataTemp.stratify(numFolds);
		}

		// for (Enumeration<Instance> e = data.enumerateInstances();
		// e.hasMoreElements();){
		// System.out.println(e.nextElement().classValue());
		// }

		for (int i = 0; i < numFolds; i++) {
			Instances train = dataTemp.trainCV(numFolds, i, new Random(1));
			eval.setPriors(train);
			Classifier copiedClassifier = Classifier.makeCopy(classifier);
			copiedClassifier.buildClassifier(train);
			Instances test = dataTemp.testCV(numFolds, i);
			eval.evaluateModel(copiedClassifier, test);
			System.out.println(eval.pctCorrect());
		}

		System.out.println(eval.pctCorrect());

		
	}
	
	public static void main(String[] args) throws Exception {

		Test test = new Test();
		test.testClassificationPercent();

	}

}
