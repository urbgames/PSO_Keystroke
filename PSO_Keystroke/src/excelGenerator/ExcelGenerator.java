package excelGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ExcelGenerator {

	// DIRECTORY TO SAVE METRIC GA .XLS
	private String fileName = "";
	private HSSFWorkbook workbook;
	private HSSFSheet sheetInfoGA;

	public ExcelGenerator(String order) {
		this.fileName += order + ".xls";
		workbook = new HSSFWorkbook();
		sheetInfoGA = workbook.createSheet("PSO");
	}

	public void insertCellInfo(int row, int cell, Object info, int cellTypeNumeric) {
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
	}

	public void saveFile() {
		try {
			System.out.println(fileName);
			FileOutputStream outputStream = new FileOutputStream(new File(fileName));
			workbook.write(outputStream);
			outputStream.close();
		} catch (IOException e) {
			System.err.println(e.toString());
		}
	}
}
