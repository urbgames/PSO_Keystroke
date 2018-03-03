package thread;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import excelGenerator.ExcelGenerator;
import systemStatusControl.StatusControl;
import systemStatusControl.StatusPSO;

public class ThreadManager extends Thread {

	private BufferedReader br;
	private int repetition, maxInteration, sizePopulation;
	private static boolean finished = false;
	private Map<String, Integer> listClassifierCountThreadTotal, listClassifierCountThreadCurrent,
			listClassifierExecuted;
	private boolean recoveryStatusPSO;

	public ThreadManager() {
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

		if (recoveryStatusPSO) {
			List<StatusPSO> statusPSOs = StatusControl.readStatus();
			sizePopulation = statusPSOs.get(0).getSizePopulation();
			maxInteration = statusPSOs.get(0).getMaxInteretor();
			for (StatusPSO statusPSO : statusPSOs) {
				if (statusPSO.getCurrentInteretor() + 1 < statusPSO.getMaxInteretor()) {
					new ClassificatorThread(sizePopulation, maxInteration, statusPSO.getRepetition(), this,
							statusPSO.getClassifier(), statusPSO);
					listClassifierCountThreadCurrent.put(statusPSO.getClassifier(),
							listClassifierCountThreadCurrent.get(statusPSO.getClassifier()) + 1);
				}
			}
			Map<String, List<StatusPSO>> statusPSOMap = statusPSOs.stream()
					.collect(Collectors.groupingBy(w -> w.getClassifier()));
			for (String classifier : statusPSOMap.keySet()){
				int size = statusPSOMap.get(classifier).size();
				listClassifierExecuted.put(classifier, statusPSOMap.get(classifier).size());
				}

		}
		else{
			StatusControl.clearFiles();
			ExcelGenerator.clearFiles();
		}
		listClassifierCountThreadTotal.forEach((classifier, value) -> {
			while (listClassifierCountThreadCurrent.get(classifier) < value) {
				createThreadClassifier(classifier);
			}
		});

		this.start();
		
	}

	private void loadFileConfig() {

		try {
			br = new BufferedReader(new FileReader("config.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			while (br.ready()) {
				String line = br.readLine();
				System.out.println(line);
				if (line.startsWith("%"))
					continue;
				else if (line.startsWith("RECOVERY"))
					this.recoveryStatusPSO = Boolean.parseBoolean(line.split(":")[1]);
				else if (line.startsWith("REPETITION"))
					this.repetition = Integer.parseInt(line.split(":")[1]);
				else if (line.startsWith("MAXINTERATION"))
					this.maxInteration = Integer.parseInt(line.split(":")[1]);
				else if (line.startsWith("SIZEPOPULATION"))
					this.sizePopulation = Integer.parseInt(line.split(":")[1]);
				else if (line.startsWith("-")) {
					String key = line.split(":")[0].substring(1, line.split(":")[0].length());
					listClassifierCountThreadTotal.put(key, Integer.parseInt(line.split(":")[1]));
					listClassifierExecuted.put(key, 0);
					listClassifierCountThreadCurrent.put(key, 0);
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	public void update(Object classifier) {
		listClassifierCountThreadCurrent.put((String) classifier, listClassifierCountThreadCurrent.get(classifier) - 1);
		System.out.println(classifier + " " + listClassifierCountThreadCurrent.get(classifier));

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
		new ClassificatorThread(sizePopulation, maxInteration, listClassifierExecuted.get(classifier), this,
				classifier);
		listClassifierExecuted.put(classifier, listClassifierExecuted.get(classifier) + 1);
		listClassifierCountThreadCurrent.put(classifier, listClassifierCountThreadCurrent.get(classifier) + 1);
	}

}
