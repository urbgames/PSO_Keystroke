package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import main.Particle;
import systemStatusControl.StatusControl;
import systemStatusControl.StatusPSO;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

//##CÓDIGO ALTERADO
//TAG PARA ALTERAÇÃO DE CÓDIGO

public class Test {

	private Instances data;
	private Classifier classifier;
	private List<StatusPSO> list;

	public Test() throws Exception {

		listFilesDirectory();

	}

	public void listFilesDirectory() {
		File[] files = new File("./_saved").listFiles();
		for (File file : files) {
			if (file.isFile()) {
				String fileName = file.getName();
				if (fileName.contains("_Experimento") && fileName.endsWith(".xls")) {
					System.out.println(file.getName());
					file.delete();
				}
			}
			if (file.isFile()) {
				String fileName = file.getName();
				if (fileName.equals("status.json")) {
					System.out.println(file.getName());
					file.delete();
				}
			}
		}
	}

	public void insertValueExcel(int rowNumber, int columnNumber, String value)
			throws FileNotFoundException, IOException {

		String pathFile = "excelTest.xls";
		HSSFWorkbook workbook;
		HSSFSheet sheetInfoGA;
		if (new File(pathFile).exists()) {
			workbook = new HSSFWorkbook(new FileInputStream(pathFile));
			sheetInfoGA = workbook.getSheetAt(0);
		} else {
			workbook = new HSSFWorkbook();
			sheetInfoGA = workbook.createSheet("test");
		}
		Row row = sheetInfoGA.createRow(rowNumber);
		Cell cell = row.createCell(columnNumber);
		cell.setCellValue(value);

		FileOutputStream outputStream2 = new FileOutputStream(new File(pathFile));
		workbook.write(outputStream2);
		outputStream2.close();

	}

	public void testExcelFile() throws IOException {

		insertValueExcel(1, 1, "testando1");

		// System.out.println(new File("excelTest.xls").exists());
		//
		// HSSFWorkbook workbook = new HSSFWorkbook();
		// HSSFSheet sheetInfoGA = workbook.createSheet("test");
		// Row row = sheetInfoGA.createRow(0);
		// Cell cell = row.createCell(0);
		// cell.setCellValue("testando1");
		// FileOutputStream outputStream = new FileOutputStream(new
		// File("excelTest.xls"));
		// workbook.write(outputStream);
		// outputStream.close();
		//
		// HSSFWorkbook workbooo2 = new HSSFWorkbook(new
		// FileInputStream("excelTest.xls"));
		// HSSFSheet sheetInfoGA2 = workbooo2.getSheetAt(0);
		// Row row2 = sheetInfoGA2.getRow(0);
		// Cell cell2 = row2.createCell(1);
		// cell2.setCellValue("testando2");
		// FileOutputStream outputStream2 = new FileOutputStream(new
		// File("excelTest.xls"));
		// workbooo2.write(outputStream2);
		// outputStream2.close();

	}

	public void testObjectTOJson() {
		List<Particle> particlesList = new ArrayList<>();
		List<StatusPSO> statusPSO = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		for (int i = 0; i < 20; i++) {
			particlesList.add(new Particle(30, i));
		}

		particlesList.get(0).getPosition()[0] = -1.123123;
		particlesList.get(0).getPosition()[2] = -1.123123;
		particlesList.get(0).getPosition()[3] = -1.123123;
		particlesList.get(0).getPosition()[1] = -1.123123;
		particlesList.get(0).getPosition()[4] = -1.123123;

		for (int i = 0, j = 0; i < 3; i++, j++) {
			statusPSO.add(new StatusPSO(particlesList, 1, 10, j, 32, 81768712, "BAYESNET"));
		}
		for (int i = 0, j = 0; i < 10; i++, j++) {
			statusPSO.add(new StatusPSO(particlesList, 1, 10, j, 32, 81768712, "MLP"));
		}

		Map<String, List<StatusPSO>> statusPSOMap = statusPSO.stream()
				.collect(Collectors.groupingBy(w -> w.getClassifier()));

		System.out.println(statusPSOMap.get("MLP").size());
		System.out.println(statusPSOMap.get("BAYESNET").size());

		StatusControl.writeStatus(statusPSO);
		try {
			list = mapper.readValue(new File("file.json"), new TypeReference<List<StatusPSO>>() {
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(list.toString());
		teste("oi", 2, 1L);

	};

	public void teste(Object... teste) {

		System.out.println(teste[1].getClass().getName());
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
		String resultado = "";
		String resultado2 = "";
		for (int i = 0; i < 10; i++) {
			double randomNum = ThreadLocalRandom.current().nextDouble(-2, 2 + 1);
			DecimalFormat numberFormat = new DecimalFormat("##.00");
			resultado += String.format("%.2f", randomNum) + ";";
			resultado2 += randomNum >=0 ? "1;" : "0;";
		}
		System.out.println(resultado);
		System.out.println(resultado2);
	}

}
