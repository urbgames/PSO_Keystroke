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
import weka.core.converters.ConverterUtils.DataSource;

public class Test {

	public static void main(String[] args) throws Exception {

		String base3 = "keystroke_71features.arff";
		Instances dataAll = new DataSource(base3).getDataSet();
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

		if (dataAll.classIndex() == -1)
			dataAll.setClassIndex(dataAll.numAttributes() - 1);
		

		LibSVM classifier = new LibSVM();
		classifier.setCost(7.46);
		classifier.setGamma(0.25);
		classifier.setNormalize(true);
		
		Evaluation eval = new Evaluation(dataAll);

		// Make a copy of the data we can reorder
		Instances data = new Instances(dataAll);

		System.out.println(data.numInstances());

		int numFolds = 2;
//		data.randomize(new Random(1));
		if (data.classAttribute().isNominal()) {
			data.stratify(numFolds);
		}
		
//		for (Enumeration<Instance> e = data.enumerateInstances(); e.hasMoreElements();){
//			   System.out.println(e.nextElement().classValue());
//		}

		for (int i = 0; i < numFolds; i++) {
			Instances train = data.trainCV(numFolds, i, new Random(1));
			System.out.println(train.numInstances());
			eval.setPriors(train);
			Classifier copiedClassifier = Classifier.makeCopy(classifier);
			copiedClassifier.buildClassifier(train);
			Instances test = data.testCV(numFolds, i);
			eval.evaluateModel(copiedClassifier, test);
			System.out.println(eval.pctCorrect());
		}
		

		System.out.println(eval.pctCorrect());


	}

}
