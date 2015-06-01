package model.Attendance;

import gui.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	public void exportData(File path, List<Attendance> data,
			List<AttendanceInfo> records) throws FileNotFoundException,
			IOException, ParseException {

		if (!"xlsx".equals(Utils.getExtension(path.getName()))) {
			path = new File(path.getName() + ".xlsx");
		}

		XSSFWorkbook workbook = null;

		if (path.isFile()) {
			workbook = new XSSFWorkbook(new FileInputStream(path));
		} else {
			workbook = new XSSFWorkbook();
		}
		
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);

		String description = month + "_" + day;

		XSSFSheet sheet = workbook.createSheet("log"+description);
		createHead(sheet);

		genData(sheet, 1, data);

		XSSFSheet recordSheet = workbook.cloneSheet(0);

		workbook.setSheetName(workbook.getNumberOfSheets()-1, "record"+description);
		
		if (records != null) {
			genRecord(recordSheet, 1, records);
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			workbook.write(fos);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void genRecord(XSSFSheet sheet, int i, List<AttendanceInfo> data)
			throws ParseException {
		String lastRowId = "";

		String lastDate = "";

		int index = i + 1;
		int colIndex = 0;
		Row record = null;
		Row dateRow = null;
		Row timeRow = null;
		Row typeRow = null;
		Row overTimeRow = null;
		Row test = null;
		Row lateTimeRow = null;
		int totalRowIndex = 0;
		double totalLateTime = 0;
		double totalOverHours = 0;
		for (AttendanceInfo row : data) {
			if (lastRowId.equals(row.getId())) {

				int diffDays = DateUtil.diffDays(lastDate, row.getDate());

				for (int diff = 1; diff < diffDays; diff++) {

					String nextDay = DateUtil.addDay(lastDate, diff);
					dateRow.createCell(colIndex).setCellValue(nextDay);

					if (!DateUtil.isDayoff(nextDay)) {
						typeRow.createCell(colIndex).setCellValue("無紀錄");
					}
					colIndex += 2;
				}

				lastDate = row.getDate();
				dateRow.createCell(colIndex).setCellValue(row.getDate());
				timeRow.createCell(colIndex).setCellValue(row.getCheckin());
				timeRow.createCell(colIndex + 1)
						.setCellValue(row.getCheckout());
				typeRow.createCell(colIndex + 1).setCellValue(
						!"0".equals(row.getOverTime()) ? "加班" : "");

				overTimeRow.createCell(colIndex + 1).setCellValue(
						row.getOverTime());
				lateTimeRow.createCell(colIndex)
						.setCellValue(row.getLateTime());

				totalLateTime += Double.parseDouble(row.getLateTime());
				totalOverHours += Double.parseDouble(row.getOverTime());

				colIndex += 2;

			} else {
				if (!isFirstRecord(index, i)) {
					totalRowIndex = index - 2;
					writeSumaryData(sheet, totalRowIndex, totalLateTime,
							totalOverHours);
				}

				record = sheet.createRow(index);
				record.createCell(0).setCellValue("姓名");
				record.createCell(1).setCellValue(row.getName());

				dateRow = sheet.createRow(index + 1);
				dateRow.createCell(0).setCellValue("日期");

				timeRow = sheet.createRow(index + 2);
				timeRow.createCell(0).setCellValue("時間");

				typeRow = sheet.createRow(index + 3);
				typeRow.createCell(0).setCellValue("請假/加班");

				overTimeRow = sheet.createRow(index + 4);
				overTimeRow.createCell(0).setCellValue("加班時數");

				test = sheet.createRow(index + 5);
				test.createCell(0).setCellValue("請假時數");

				lateTimeRow = sheet.createRow(index + 6);
				lateTimeRow.createCell(0).setCellValue("遲到時間");

				lastRowId = row.getId();
				index += 10;

				dateRow.createCell(1).setCellValue(row.getDate());

				timeRow.createCell(1).setCellValue(row.getCheckin());
				timeRow.createCell(2).setCellValue(row.getCheckout());

				typeRow.createCell(2).setCellValue(
						!"0".equals(row.getOverTime()) ? "加班" : "");

				overTimeRow.createCell(2).setCellValue(row.getOverTime());
				lateTimeRow.createCell(1).setCellValue(row.getLateTime());

				totalLateTime = Double.parseDouble(row.getLateTime());
				totalOverHours = Double.parseDouble(row.getOverTime());
				colIndex = 3;

				lastDate = row.getDate();
			}
		}

		writeSumaryData(sheet, index - 2, totalLateTime, totalOverHours);
	}

	private boolean isFirstRecord(int index, int i) {
		return index == i + 1;
	}

	private void writeSumaryData(XSSFSheet sheet, int totalRowIndex,
			double totalLateTime, double totalOverHours) {
		Row totalLateRow = sheet.createRow(totalRowIndex);
		totalLateRow.createCell(0).setCellValue("遲到總分鐘");
		totalLateRow.createCell(1).setCellValue(totalLateTime);

		Row totalOverRow = sheet.createRow(totalRowIndex + 1);
		totalOverRow.createCell(0).setCellValue("加班總時數");
		totalOverRow.createCell(1).setCellValue(totalOverHours);
	}

	private void createHead(XSSFSheet sheet) {
		String[] columns = { "ID", "Name", "Senor", "Time", "TYPE" };
		Row head = sheet.createRow(0);
		int index = 0;
		for (String column : columns) {
			Cell cell = head.createCell(index++);
			cell.setCellValue(column);
		}
	}

	private void genData(XSSFSheet sheet, int rownum, List<Attendance> data) {
		for (Attendance column : data) {
			Row row = sheet.createRow(rownum++);

			Cell key = row.createCell(0);
			key.setCellValue((String) column.getId());

			Cell name = row.createCell(1);
			name.setCellValue((String) column.getName());

			Cell sener = row.createCell(2);
			sener.setCellValue(column.getSenor());

			Cell date = row.createCell(3);
			date.setCellValue(column.getTime());

			Cell type = row.createCell(4);
			type.setCellValue(column.getType());
		}
	}

}
