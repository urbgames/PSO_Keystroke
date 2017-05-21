package classificator;

import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.RemovePercentage;

public final class Classification {

	private String base3 = "keystroke_71features.arff";
	private String base4 = "C:\\Users\\Urbgames\\Documents\\keystroke_normalized_1.arff";
	private static Instances dataAll = null;
	private String baseCurrent = base3;
	public int seed;
	private Classifier classifier;
	private String classifierName;
	public static final String BAYESNET = "BAYESNET";
	public static final String NAIVEBAYES = "NAIVEBAYES";
	public static final String J48 = "J48";
	public static final String KNN = "KNN";
	public static final String SVM = "SVM";
	public static final String RANDOMFOREST = "RANDOMFOREST";
	public static final String MLP = "MLP";

	public int getLength() {
		return dataAll.numAttributes() - 1;
	}

	// private static Classification getInstance() throws Exception {
	//
	// if (classification == null) {
	// synchronized (Classification.class) {
	// if (classification == null) {
	// classification = new Classification();
	// }
	// }
	// }
	//
	// return classification;
	// }

	public ResultClassification getFitnessClafissation(boolean[] binaryGenes) throws Exception {

		Instances dataTemp = dataAll;
		int count = 0;
		String optionsRemove = "";

		for (int i = 0; i < binaryGenes.length; i++) {
			if (!binaryGenes[i]) {
				count++;
				optionsRemove += "" + (i + 1);
				optionsRemove += ",";
			}
		}

		if (count != 0)
			optionsRemove = optionsRemove.substring(0, optionsRemove.length() - 1);

		String[] options = new String[2];
		options[0] = "-R";
		options[1] = optionsRemove;

		Remove remove = new Remove();
		remove.setOptions(options);
		remove.setInputFormat(dataTemp);

		dataTemp = Filter.useFilter(dataTemp, remove);

		return classification(dataTemp);
	}

	private ResultClassification classification(Instances data) throws Exception {

		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);

		// VALIDAÇÃO CRUZADA
		/*Evaluation eval = new Evaluation(data);
		try {
			eval.crossValidateModel(classifier, data, 10, new Random(seed));
		} catch (Exception e) {
			eval.crossValidateModel(classifier, data, 10, new Random(seed));
		}*/

		data.randomize(new Random(seed));
		
		RemovePercentage percentageData = new RemovePercentage();
		percentageData.setInputFormat(data);
		percentageData.setOptions(Utils.splitOptions("-P 90"));
		Instances dataTest = Filter.useFilter(data, percentageData);
		
		percentageData.setOptions(Utils.splitOptions("-V -P 90"));
		Instances dataTrain = Filter.useFilter(data, percentageData);
		
		classifier.buildClassifier(dataTrain);
		Evaluation eval = new Evaluation(dataTrain);
		eval.evaluateModel(classifier, dataTest);

		double avgFAR = 0, avgFRR = 0;
		for (int i = 0; i < data.numClasses(); i++) {
			avgFAR += eval.falsePositiveRate(i);
			avgFRR += eval.falseNegativeRate(i);
		}
		avgFAR = avgFAR / (data.numClasses());
		avgFAR *= 100;
		avgFRR = avgFRR / (data.numClasses());
		avgFRR *= 100;

		ResultClassification classification = new ResultClassification();
		classification.setFAR(avgFAR);
		classification.setFRR(avgFRR);
		classification.setPctCorrect(eval.pctCorrect());

		return classification;

	}

	public Classification(String classifier) throws Exception {
		if (dataAll == null) {
			dataAll = new DataSource(baseCurrent).getDataSet();
		}
		
		seed = new Random().nextInt();
		
		this.classifierName = classifier;
		switch (classifier) {
		case BAYESNET:
			this.classifier = new BayesNet();
			break;
		case NAIVEBAYES:
			this.classifier = new NaiveBayes();
			break;
		case J48:
			weka.classifiers.trees.J48 j48 = new weka.classifiers.trees.J48();
			j48.setConfidenceFactor((float) 0.2);
			j48.setMinNumObj(4);
			this.classifier = j48;
			break;
		case KNN:
			this.classifier = new IBk(1);
			break;
		case SVM:
			LibSVM svm = new LibSVM();
			svm.setCost(7.46);
			svm.setGamma(0.25);
			svm.setNormalize(true);
			this.classifier = svm;
			break;
		case RANDOMFOREST:
			this.classifier = new RandomForest();
			break;
		case MLP:
			this.classifier = new MultilayerPerceptron();
			break;
		default:
			this.classifier = null;
			break;
		}

	}

//	private void changeSeed() throws IOException {
//		seed = new Random().nextInt();
//	}

	public String getClassifierName() {
		return classifierName;
	}

	public void setClassifierName(String classifierName) {
		this.classifierName = classifierName;
	}

	public int getSeed() {
		return seed;
	}

}
