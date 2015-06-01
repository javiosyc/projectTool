import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import model.Attendance.Attendance;
import model.Attendance.AttendanceInfo;
import model.Attendance.AttendanceInfoUtil;
import model.Attendance.ExcelUtil;
import au.com.bytecode.opencsv.CSVReader;

public class TestImport {
	public static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) {
		CSVReader reader = null;
		try {
			reader = new CSVReader(new BufferedReader(new InputStreamReader(
					new FileInputStream("test.csv"), "big5")), '\t');
			List<String[]> records = reader.readAll();

			TreeSet<Attendance> db = new TreeSet<Attendance>();
			for (String[] record : records) {
				Attendance row = new Attendance();
				row.setId(record[0]);
				row.setName(record[4]);
				row.setSenor(record[2]);
				row.setTime(record[1]);
				row.setType(record[5]);
				db.add(row);
			}

			ExcelUtil util = new ExcelUtil();

			List<AttendanceInfo> info = AttendanceInfoUtil
					.parseAttendanceLog(new ArrayList<Attendance>(db));

			util.exportData(new File("test"), new ArrayList<Attendance>(db),
					info);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
