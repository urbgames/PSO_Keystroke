package thread;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class ThreadManager extends Thread{

	private BufferedReader br;
	private int repetition, maxInteration, sizePopulation;
	private static boolean finished = false;
	private Map<String, Integer> listClassifierCountThreadTotal, listClassifierCountThreadCurrent,
			listClassifierExecuted;

	public ThreadManager() throws FileNotFoundException {
		super(new Runnable() {
			public void run() {
				while (!finished) {
				}
			}
		});

		listClassifierCountThreadTotal = new HashMap<String, Integer>();
		listClassifierCountThreadCurrent = new HashMap<String, Integer>();
		listClassifierExecuted = new HashMap<String, Integer>();

		loadFileConfig();

		listClassifierCountThreadTotal.forEach((classifier, value) -> {
			while (listClassifierCountThreadCurrent.get(classifier) < value) {
				createThreadClassifier(classifier);
				listClassifierCountThreadCurrent.put(classifier, listClassifierCountThreadCurrent.get(classifier) + 1);
			}
		});

		this.start();
	}

	private void loadFileConfig() throws FileNotFoundException {

		br = new BufferedReader(new FileReader("config.txt"));
		try {
			while (br.ready()) {
				String line = br.readLine();
				if (line.startsWith("%"))
					continue;
				else if (line.startsWith("REPETITION"))
					this.repetition = Integer.parseInt(line.split(":")[1]);
				else if (line.startsWith("MAXINTERATION"))
					this.maxInteration = Integer.parseInt(line.split(":")[1]);
				else if (line.startsWith("SIZEPOPULATION"))
					this.sizePopulation = Integer.parseInt(line.split(":")[1]);
				else if (line.startsWith("-")) {
					listClassifierCountThreadTotal.put(line.split(":")[0].substring(1, line.split(":")[0].length()),
							Integer.parseInt(line.split(":")[1]));
					listClassifierExecuted.put(line.split(":")[0].substring(1, line.split(":")[0].length()), 0);
					listClassifierCountThreadCurrent.put(line.split(":")[0].substring(1, line.split(":")[0].length()), 0);
				}
			}
		} catch (Exception exception) {
			System.err.println(exception.getMessage());
		}
	
	}

	public void update(Object classifier) {
		listClassifierCountThreadCurrent.put((String) classifier, listClassifierCountThreadCurrent.get(classifier) - 1);

		if (listClassifierExecuted.get((String) classifier) < repetition) {
			createThreadClassifier((String) classifier);
		}

		boolean isFinish = true;
		for (Map.Entry<String, Integer> entry : listClassifierExecuted.entrySet()) {
			if (entry.getValue() < repetition) {
				isFinish = false;
				break;
			}
		}
		if (isFinish)
			stop();
	}

	private void createThreadClassifier(String classifier) {
		new ClassificatorThread(sizePopulation, maxInteration, listClassifierExecuted.get(classifier), this, classifier);
		listClassifierExecuted.put((String) classifier, listClassifierExecuted.get(classifier) + 1);
	}

}
