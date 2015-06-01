package gui.Attendance;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import model.Attendance.Attendance;

public class AttendanceTablePanel extends JPanel {

	private static final long serialVersionUID = -895376974642630155L;
	private JTable table;

	private AttendanceModel tableModel;

	public AttendanceTablePanel() {

		tableModel = new AttendanceModel();
		table = new JTable(tableModel);
		table.getColumn("No.").setPreferredWidth(50);
		table.getColumn("ID").setPreferredWidth(150);
		table.getColumn("Name").setPreferredWidth(150);
		table.getColumn("Time").setPreferredWidth(300);
		table.getColumn("Type").setPreferredWidth(70);

		add(new JScrollPane(table));

	}

	public void setData(List<Attendance> db) {
		tableModel.setData(db);
	}

	public void refresh() {
		tableModel.fireTableDataChanged();
	}

}
