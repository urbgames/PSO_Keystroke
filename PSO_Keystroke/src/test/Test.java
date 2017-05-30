package test;

import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

//##CÓDIGO ALTERADO
//TAG PARA ALTERAÇÃO DE CÓDIGO

public class Test {

	private Instances data;
	private Classifier classifier;

	public Test() throws Exception {
		String base3 = "keystroke_71features.arff";
		data = new DataSource(base3).getDataSet();
		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);

		MultilayerPerceptron mlp = new MultilayerPerceptron();
		mlp.setLearningRate(0.05);
		mlp.setMomentum(0.3);
		mlp.setValidationThreshold(20);
		mlp.setValidationSetSize(30);
		mlp.setTrainingTime(5000);
//		mlp.setDecay(true);
		classifier = mlp;
	}

	public void splitValidation() throws Exception {

		data.randomize(new Random());

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

	public void crossValidation() throws Exception {

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

		double start = System.currentTimeMillis();

		Test test = new Test();
		test.splitValidation();

		System.out.println(System.currentTimeMillis() - start);
	}

}
