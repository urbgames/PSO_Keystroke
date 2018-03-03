package classificator;

public class FunctionTest {

	public static ResultClassification getFitness(boolean[] binaryGenes) {

		int countFitness = 0;

		for (int i = 0; i < binaryGenes.length; i++) {
			if ((i % 2 == 0) == binaryGenes[i])
				countFitness++;
		}

		ResultClassification resultClassification = new ResultClassification();
		resultClassification.setFAR(0);
		resultClassification.setFRR(0);
		resultClassification.setPctCorrect(countFitness);
		
		return resultClassification;
	}

}
