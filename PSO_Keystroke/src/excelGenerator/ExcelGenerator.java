package excelGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ExcelGenerator {

	private String fileName = "";
	// private HSSFWorkbook workbook;
	// private HSSFSheet sheetInfoGA;

	public static void clearFiles() {
		File[] files = new File(".").listFiles();
		for (File file : files) {
			if (file.isFile()) {
				String fileName = file.getName();
				if (fileName.contains("_Experimento") && fileName.endsWith(".xls")) {
					System.out.println(file.getName());
					file.delete();
				}
			}
		}
	}

	public ExcelGenerator(String order) {
		this.fileName += order + ".xls";
		// workbook = new HSSFWorkbook();
		// sheetInfoGA = workbook.createSheet("PSO");
	}

	public synchronized void insertCellInfo(int row, int cell, Object info, int cellTypeNumeric) {

		HSSFWorkbook workbook = null;
		HSSFSheet sheetInfoGA = null;
		if (new File(fileName).exists()) {
			try {
				workbook = new HSSFWorkbook(new FileInputStream(fileName));
				sheetInfoGA = workbook.getSheetAt(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			workbook = new HSSFWorkbook();
			sheetInfoGA = workbook.createSheet("PSO");
		}

		Row row2;
		if (sheetInfoGA.getRow(row) == null) {
			row2 = sheetInfoGA.createRow(row);
		}
		row2 = sheetInfoGA.getRow(row);
		sheetInfoGA.autoSizeColumn(cell);
		Cell cell2 = row2.createCell(cell);
		if (cellTypeNumeric == Cell.CELL_TYPE_NUMERIC)
			cell2.setCellValue(Double.parseDouble(String.valueOf(info)));
		else
			cell2.setCellValue(String.valueOf(info));

		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(new File(fileName));
			workbook.write(outputStream);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
