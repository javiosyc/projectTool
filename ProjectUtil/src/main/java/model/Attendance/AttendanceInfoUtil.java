package model.Attendance;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceInfoUtil {

	public static List<AttendanceInfo> parseAttendanceLog(List<Attendance> db)
			throws ParseException {

		String lastRowId = "";
		String lastDate = "";
		List<AttendanceInfo> result = new ArrayList<AttendanceInfo>();

		for (Attendance row : db) {
			if (lastRowId.equals(row.getId())
					&& lastDate.equals(DateUtil.getDate(row.getTime()))) {
				updateAttendanceInfo(result, row);
			} else {
				createAttendanceInfo(result, row);
			}

			lastRowId = row.getId();
			lastDate = DateUtil.getDate(row.getTime());
		}

		return result;
	}

	private static void updateAttendanceInfo(List<AttendanceInfo> result,
			Attendance row) {

		AttendanceInfo record = result.get(result.size() - 1);
		record.addLog(row.getTime());
	}

	private static void createAttendanceInfo(List<AttendanceInfo> result,
			Attendance row) throws ParseException {

		AttendanceInfo info = new AttendanceInfo();

		String date = DateUtil.getDate(row.getTime());

		info.setType(DateUtil.isDayoff(date) ? "dayoff" : "workday");
		info.setId(row.getId());
		info.setName(row.getName());
		info.setDate(DateUtil.getDate(row.getTime()));

		info.addLog(row.getTime());
		result.add(info);
	}

}
