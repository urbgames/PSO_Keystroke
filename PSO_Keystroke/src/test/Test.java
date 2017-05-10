package test;

import java.util.Random;

import classificator.Classification;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Test {

	public static void main(String[] args) throws Exception {

		String base3 = "keystroke_71features.arff";
		Instances data = new DataSource(base3).getDataSet();
//		
//	BayesNet classifier = new BayesNet();
//		
		
		LibSVM classifier = new LibSVM();
		classifier.setCost(7.46);
		classifier.setGamma(0.25);
		classifier.setNormalize(true);
		
		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);
		// VALIDAÇÃO CRUZADA
		Evaluation eval = new Evaluation(data);
		try {
			eval.crossValidateModel(classifier, data, 10, new Random(1));
		} catch (Exception e) {
			eval.crossValidateModel(classifier, data, 10, new Random(1));
		}

		System.out.println(eval.pctCorrect());


//		Classification classification = new Classification(Classification.BAYESNET);
//		int seed = classification.getSeed();
//		Classification classification2 = new Classification(Classification.NAIVEBAYES);
//		int seed2 = classification2.getSeed();
//		System.out.println("oi");
	}

}
