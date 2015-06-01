package model.Attendance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import au.com.bytecode.opencsv.CSVReader;

public class AttendanceUtil {

	public List<Attendance> readData(File file) {

		CSVReader reader = null;
		try {
			reader = new CSVReader(new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "big5")), '\t');
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

			return new ArrayList<Attendance>(db);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
		return new ArrayList<Attendance>();
	}

}
