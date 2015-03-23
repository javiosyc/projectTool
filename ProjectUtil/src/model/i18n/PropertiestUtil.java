package model.i18n;

import gui.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeSet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PropertiestUtil {

	public List<TransMap> getPropValues(String... paths) throws IOException {
		TreeSet<TransMap> result = new TreeSet<TransMap>();
		LanguageCode languageCode;

		Map<String, Properties> props = new HashMap<String, Properties>();

		for (String path : paths) {
			languageCode = LanguageCode.parsePath(path);

			Properties prop = new Properties();
			String propFileName = path;
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '"
						+ propFileName + "' not found in the classpath");
			}

			Iterator<Entry<Object, Object>> it = prop.entrySet().iterator();

			while (it.hasNext()) {
				Entry<Object, Object> entry = it.next();
				TransMap record = new TransMap();

				String key = entry.getKey().toString();
				String message = entry.getValue().toString();

				record.setKey(key);

				if (result.contains(record)) {
					TransMap map = result.tailSet(record).first();
					map.getTranslations().put(languageCode, message);
				} else {
					Map<LanguageCode, String> newTrans = new HashMap<LanguageCode, String>();
					newTrans.put(languageCode, message);

					record.setTranslations(newTrans);
					result.add(record);
				}
			}
		}
		return new ArrayList<TransMap>(result);
	}

	public void exportData(File path, List<TransMap> data) {

		if (!"xlsx".equals(Utils.getExtension(path.getName()))) {
			path = new File(path.getName() + ".xlsx");
		}

		XSSFWorkbook workbook = new XSSFWorkbook();

		// Create a blank sheet
		XSSFSheet sheet = workbook.createSheet("transField");

		createHead(sheet);

		genData(sheet, 1, data);

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

	private void genData(XSSFSheet sheet, int rownum, List<TransMap> data) {
		for (TransMap trans : data) {
			Row row = sheet.createRow(rownum++);

			Cell key = row.createCell(0);
			key.setCellValue((String) trans.getKey());

			Iterator<Entry<LanguageCode, String>> it = trans.getTranslations()
					.entrySet().iterator();

			while (it.hasNext()) {
				Entry<LanguageCode, String> record = it.next();

				switch (record.getKey()) {

				case zh_TW:
					Cell zh = row.createCell(1);
					zh.setCellValue((String) record.getValue());
					break;
				case zh_CN:
					Cell cn = row.createCell(2);
					cn.setCellValue((String) record.getValue());
					break;
				case vn:
					Cell vn = row.createCell(4);
					vn.setCellValue((String) record.getValue());
					break;
				case en:
					Cell en = row.createCell(3);
					en.setCellValue((String) record.getValue());
					break;
				}

			}
		}

	}

	private void createHead(XSSFSheet sheet) {
		String[] columns = { "Key", "繁中", "簡中", "英文", "越南文" };
		Row head = sheet.createRow(0);
		int index = 0;
		for (String column : columns) {
			Cell cell = head.createCell(index++);
			cell.setCellValue(column);
		}
	}

	public List<TransMap> getPropValues(File[] selectedFile) throws IOException {

		String[] selectedName = new String[selectedFile.length];
		String defaultFolder = System.getProperty("user.dir") + File.separator
				+ "src" + File.separator;

		for (int i = 0; i < selectedFile.length; i++) {
			String path = selectedFile[i].toString();
			int index = path.indexOf(defaultFolder);

			if (index == -1) {
				throw new IOException();
			}

			selectedName[i] = path.substring(index + defaultFolder.length(),
					path.length());
		}
		return getPropValues(selectedName);
	}
}