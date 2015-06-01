package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import model.Attendance.Attendance;
import model.Attendance.AttendanceInfoUtil;
import model.Attendance.AttendanceUtil;
import model.Attendance.Database;
import model.Attendance.ExcelUtil;

public class Controller {
	Database db = new Database();

	public List<Attendance> getAttendance() {
		return db.getAttendanceLogs();
	}

	public void addAll(List<Attendance> maps) {
		db.addAll(maps);
	}

	public void clear() {
		db.clear();
	}

	public int loadFromFile(File[] selectedFile) throws IOException {

		AttendanceUtil util = new AttendanceUtil();

		List<Attendance> data = util.readData(selectedFile[0]);

		addAll(data);

		return data.size();
	}

	public void saveToFile(File file) throws FileNotFoundException,
			IOException, ParseException {

		ExcelUtil util = new ExcelUtil();
		util.exportData(file, db.getAttendanceLogs(),
				AttendanceInfoUtil.parseAttendanceLog(db.getAttendanceLogs()));
	}

	public void removenTransMap(int index) {
		db.removenTransMap(index);
	}
}
