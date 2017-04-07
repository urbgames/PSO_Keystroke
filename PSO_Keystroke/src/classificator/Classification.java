package classificator;

import java.io.IOException;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public final class Classification {

	//EXPERIMENTO01
	//private String base3 = "C:\\Users\\Urbgames\\Documents\\keystroke_71features.arff";
	private String base4 = "C:\\Users\\Urbgames\\Documents\\keystroke_normalized_1.arff";
	private static Instances dataAll = null;
	private String baseCurrent = base4;
	private static int seed = 0;
	

	private static volatile Classification classification;

	public int getLength() {
		return dataAll.numAttributes() - 1;
	}

	public static Classification getInstance() throws Exception {
		
		if (classification == null) {
			synchronized (Classification.class) {
				if (classification == null) {
					classification = new Classification();
				}
			}
		}

		return classification;
	}

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

		BayesNet classifier = new BayesNet();

		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);
		// VALIDAÇÃO CRUZADA
		Evaluation eval = new Evaluation(data);
		try {
			eval.crossValidateModel(classifier, data, 10, new Random(seed));
		} catch (Exception e) {
			eval.crossValidateModel(classifier, data, 10, new Random(seed));
		}

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

	public Classification() throws Exception {
		if (dataAll == null) {
			dataAll = new DataSource(baseCurrent).getDataSet();
		}
	}

	public void changeSeed() throws IOException {
		seed = new Random().nextInt();
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		Classification.seed = seed;
	}

}
