package model.Attendance;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;

public class DateUtil {
	public static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static final String dateFormat = "%d-%02d-%02d";

	private static Set<String> daysOf;

	public static double diffHours(String d1, String d2) throws ParseException {
		long diff = diffMillseconds(d1, d2);
		long diffMinutes = diff / (60 * 1000);

		return Math.floor((double) diffMinutes / 30) * 0.5;
	}

	public static double diffMinute(String d1, String d2) throws ParseException {
		long diff = diffMillseconds(d1, d2);
		long diffMinutes = diff / (60 * 1000);
		return diffMinutes;
	}

	public static String overHours(String checkout) throws ParseException {
		String date = checkout.substring(0, 11) + "19:00:00";

		double diff = diffHours(date, checkout);
		if (diff > 0)
			return String.valueOf(diff);
		else
			return "0";
	}

	public static String getDate(String dateString) throws ParseException {
		Date t1 = sdf.parse(dateString);

		Calendar cal = Calendar.getInstance();

		cal.setTime(t1);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int year = cal.get(Calendar.YEAR);

		return String.format(dateFormat, year, month, day);
	}

	public static String lateMins(String checkin) throws ParseException {
		String date = checkin.substring(0, 11) + "09:00:00";
		double diff = diffMinute(date, checkin);
		if (diff > 0)
			return String.valueOf(diff);
		else
			return "0";
	}

	public static boolean isDayoff(String date) {
		if (daysOf == null) {
			CSVReader reader = null;
			try {
				ClassLoader classLoader = Thread.currentThread()
						.getContextClassLoader();

				if (classLoader == null) {
					classLoader = Class.class.getClassLoader();
				}
				reader = new CSVReader(
						new BufferedReader(new InputStreamReader(classLoader
								.getResourceAsStream("holiday.csv"))));
				List<String[]> records = reader.readAll();
				daysOf = new HashSet<String>();
				for (String[] record : records) {
					daysOf.add(record[0]);
				}
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
		return daysOf.contains(date);
	}

	public static String overHoursInDayoff(String checkin, String checkout)
			throws ParseException {
		double diff = diffHours(checkin, checkout);
		if (diff > 0)
			return String.valueOf(diff);
		else
			return "0";
	}

	public static long diffMillseconds(String d1, String d2)
			throws ParseException {
		Date t1 = sdf.parse(d1);
		Date t2 = sdf.parse(d2);
		return t2.getTime() - t1.getTime();
	}

	public static int diffDays(String lastDate, String date)
			throws ParseException {
		long diff = diffMillseconds(lastDate + " 00:00:00", date + " 00:00:00 ");
		return (int) (diff / (24 * 60 * 60 * 1000));
	}

	public static String addDay(String lastDate, int i) throws ParseException {
		Date t1 = sdf.parse(lastDate + " 00:00:00");
		long t2 = t1.getTime() + i * 24 * 60 * 60 * 1000;

		return sdf.format(new Date(t2)).substring(0, 10);
	}
}
