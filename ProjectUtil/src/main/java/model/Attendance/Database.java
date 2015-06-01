package model.Attendance;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Database {
	List<Attendance> attendanceLogs;

	public Database() {
		attendanceLogs = new LinkedList<Attendance>();
	}

	public List<Attendance> getAttendanceLogs() {
		return Collections.unmodifiableList(attendanceLogs);
	}

	public void setAttendanceLogs(List<Attendance> attendanceLogs) {
		this.attendanceLogs = attendanceLogs;
	}

	public void addAll(List<Attendance> maps) {
		attendanceLogs.addAll(maps);
	}

	public void clear() {
		attendanceLogs.clear();
	}

	public void removenTransMap(int index) {
		attendanceLogs.remove(index);
	}
}
