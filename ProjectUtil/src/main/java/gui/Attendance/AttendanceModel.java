package gui.Attendance;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.Attendance.Attendance;

public class AttendanceModel extends AbstractTableModel {

	private static final long serialVersionUID = 5847817000356867584L;

	List<Attendance> db;

	String[] colNames = { "No.", "ID", "Name", "Time", "Type", "Senor" };

	@Override
	public int getRowCount() {
		return db.size();
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Attendance row = db.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return rowIndex + 1;
		case 1:
			return row.getId();
		case 2:
			return row.getName();
		case 3:
			return row.getTime();
		case 4:
			return row.getType();
		case 5:
			return row.getSenor();
		}

		return null;
	}

	public void setData(List<Attendance> db) {
		this.db = db;
	}

	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

}
