package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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
	private PropertiesFileChooser fileChooser;
	private ExcelFileChooser excelFileChooser;
	private TranslationTablePanel tablePanel;
	private Controller controller;

	private PrefsDialog prefsDailog;
	private Preferences prefs;

	private JTabbedPane tabPane;

	private MessagePanel messsagePanel;

	private static String DEFAULT_WRITE_FOLDER_KEY = "defaultWriteFolder";

	public MainFrame() {

		super("Project Tools");

		setLayout(new BorderLayout());

		toolbar = new Toolbar();
		textPanel = new TextPanel();

		controller = new Controller();

		prefsDailog = new PrefsDialog(this);
		prefs = Preferences.userRoot().node("projectTool.dir");

		initTabPanel();

		fileChooser = new PropertiesFileChooser(Utils.DEFAULT_FOLDER);
		excelFileChooser = new ExcelFileChooser(prefs.get(
				DEFAULT_WRITE_FOLDER_KEY, Utils.DEFAULT_FOLDER));

		setJMenuBar(createMenuBar());

		prefsDailog.setPrefsListener(new PrefsListener() {
			public void preferencesSet(String defaultPath) {
				prefs.put(DEFAULT_WRITE_FOLDER_KEY, defaultPath);
			}

			public void preferenceReset() {
				if (prefs != null) {
					prefs.remove(DEFAULT_WRITE_FOLDER_KEY);
					prefsDailog.setDefaulsts(Utils.DEFAULT_FOLDER);
				}
			}
		});
		String defaultPath = prefs.get(DEFAULT_WRITE_FOLDER_KEY,
				Utils.DEFAULT_FOLDER);
		prefsDailog.setDefaulsts(defaultPath);

		toolbar.setToolBarListener(new ToolBarListener() {
			public void textEmitted(String text) {
				textPanel.appendText(text);
			}

			public void fireLoadFileChooser() {
				if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					File[] selectedFile = fileChooser.getSelectedFiles();

					textPanel.appendText("selected File:\n");
					for (File file : selectedFile) {
						textPanel.appendText(file + "\n");
					}
					try {
						int count = controller.loadFromFile(selectedFile);

						tablePanel.refresh();

						textPanel.appendText("read properties size :" + count);

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
					controller.saveToFile(file);
				}
			}
		});

		add(toolbar, BorderLayout.NORTH);
		add(tabPane, BorderLayout.CENTER);
		add(textPanel, BorderLayout.SOUTH);
		setMinimumSize(new Dimension(500, 400));
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	private void initTabPanel() {
		tabPane = new JTabbedPane();

		tablePanel = new TranslationTablePanel();
		tablePanel.setData(controller.getTransMaps());
		tablePanel.setTranslationTableistener(new TranslationTableistener() {
			public void rowDeleted(int row) {
				controller.removenTransMap(row);
			}
		});

		messsagePanel = new MessagePanel();

		tabPane.addTab("Preperites Parser", tablePanel);
		tabPane.addTab("Messages Server", messsagePanel);

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
