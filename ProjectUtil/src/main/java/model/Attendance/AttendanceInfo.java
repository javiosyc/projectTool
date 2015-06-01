package model.Attendance;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceInfo {
	private String id;
	private String name;
	private List<String> log = new ArrayList<String>();
	private String date;
	private String checkin;
	private String checkout;

	private String lateTime;
	private String overTime;

	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
		this.type = DateUtil.isDayoff(date) ? "dayoff" : "workday";
	}

	public String getCheckin() {
		return checkin;
	}

	public void setCheckin(String checkin) {

		try {
			if ("workday".equals(type)) {
				this.lateTime = DateUtil.lateMins(checkin);
			} else {
				this.lateTime = "0";
			}
		} catch (ParseException e) {
			this.lateTime = "ERROR";
		}
		this.checkin = checkin;
	}

	public String getCheckout() {
		return checkout;
	}

	public void setCheckout(String checkout) {
		try {
			if ("workday".equals(type)) {
				this.overTime = DateUtil.overHours(checkout);
			} else {
				this.overTime = DateUtil.overHoursInDayoff(checkin, checkout);
			}
		} catch (ParseException e) {
			this.overTime = "ERROR";
		}
		this.checkout = checkout;
	}

	public String getLateTime() {
		return lateTime;
	}

	public String getOverTime() {
		return overTime;
	}

	public void addLog(String time) {
		if (log.size() == 0) {
			setCheckin(time);
		}
		this.log.add(time);
		setCheckout(log.get(log.size() - 1));
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
