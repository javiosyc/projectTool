package gui;

import gui.Attendance.AttendanceTablePanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import controller.Controller;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -6356926813883348241L;
	private TextPanel textPanel;
	private Toolbar toolbar;
	private CSVFileChooser fileChooser;
	private ExcelFileChooser excelFileChooser;
	private AttendanceTablePanel attendaceTable;
	private Controller controller;

	private PrefsDialog prefsDailog;
	private Preferences prefs;

	private JTabbedPane tabPane;

	private static String DEFAULT_EXPORT_FOLDER_KEY = "defaultExportFolder";

	private static String DEFAULT_IMPORT_FOLDER_KEY = "defaultImportFolder";

	public MainFrame() {

		super("Project Tools");

		setLayout(new BorderLayout());

		toolbar = new Toolbar();
		textPanel = new TextPanel();

		controller = new Controller();

		initTabPanel();

		initPrefDailog();

		setJMenuBar(createMenuBar());

		toolbar.setToolBarListener(new ToolBarListener() {
			public void textEmitted(String text) {
				textPanel.appendText(text);
			}

			public void fireLoadFileChooser() {
				if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();

					textPanel.appendText("selected File:\n");
					textPanel.appendText(selectedFile + "\n");

					try {
						int count = controller
								.loadFromFile(new File[] { selectedFile });

						attendaceTable.refresh();

						textPanel.appendText("read properties size :" + count
								+ "\n");

					} catch (IOException e) {
						JOptionPane.showMessageDialog(MainFrame.this,
								"Could not load data from file.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}

			public void fireSaveFileChooser() {
				if (excelFileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					File file = excelFileChooser.getSelectedFile();
					try {
						controller.saveToFile(file);
					} catch (FileNotFoundException e) {
						textPanel.appendText(e + "\n");
					} catch (IOException e) {
						textPanel.appendText(e + "\n");
					} catch (ParseException e) {
						textPanel.appendText(e + "\n");
					}
				}
			}
		});

		add(toolbar, BorderLayout.NORTH);
		add(tabPane, BorderLayout.CENTER);
		add(textPanel, BorderLayout.SOUTH);
		setMinimumSize(new Dimension(500, 400));
		setSize(700, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	private void initPrefDailog() {
		prefs = Preferences.userRoot().node("projectTool.dir");

		prefsDailog = new PrefsDialog(this);
		prefsDailog.setPrefsListener(new PrefsListener() {
			public void setPreferences(String defaultImportPath,
					String defaultExportPath) {
				prefs.put(DEFAULT_EXPORT_FOLDER_KEY, defaultExportPath);
				prefs.put(DEFAULT_IMPORT_FOLDER_KEY, defaultImportPath);
				fileChooser.setCurrentDirectory(new File(defaultImportPath));
				excelFileChooser
						.setCurrentDirectory(new File(defaultExportPath));
			}

			@Override
			public String getDefaultExporPath() {
				return prefs.get(DEFAULT_EXPORT_FOLDER_KEY,
						Utils.DEFAULT_FOLDER);
			}

			@Override
			public String getDefaultImporPath() {
				return prefs.get(DEFAULT_IMPORT_FOLDER_KEY,
						Utils.DEFAULT_FOLDER);
			}
		});

		String exportPath = prefs.get(DEFAULT_EXPORT_FOLDER_KEY,
				Utils.DEFAULT_FOLDER);

		String importPath = prefs.get(DEFAULT_IMPORT_FOLDER_KEY,
				Utils.DEFAULT_FOLDER);

		prefsDailog.setDefaultImportPath(importPath);

		prefsDailog.setDefaultExportPath(exportPath);

		fileChooser = new CSVFileChooser(importPath);

		excelFileChooser = new ExcelFileChooser(exportPath);

	}

	private void initTabPanel() {
		tabPane = new JTabbedPane();
		attendaceTable = new AttendanceTablePanel();
		attendaceTable.setData(controller.getAttendance());
		tabPane.addTab("Check In/Out Log", attendaceTable);
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		final JMenu fileMenu = new JMenu("File");

		JMenuItem prefsItem = new JMenuItem("Preferencess");

		fileMenu.add(prefsItem);

		menuBar.add(fileMenu);
		prefsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prefsDailog.setVisible(true);
			}
		});

		return menuBar;
	}

}
