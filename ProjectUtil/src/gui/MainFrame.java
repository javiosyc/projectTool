package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controller.Controller;

public class MainFrame extends JFrame {
	private TextPanel textPanel;
	private Toolbar toolbar;
	private PropertiesFileChooser fileChooser;
	private ExcelFileChooser excelFileChooser;
	private TranslationTablePanel tablePanel;
	private Controller controller;

	public MainFrame() {

		super("Project Tools");

		setLayout(new BorderLayout());

		toolbar = new Toolbar();
		textPanel = new TextPanel();
		tablePanel = new TranslationTablePanel();
		controller = new Controller();

		tablePanel.setData(controller.getTransMaps());
		tablePanel.setTranslationTableistener(new TranslationTableistener() {
			public void rowDeleted(int row) {
				controller.removenTransMap(row);
			}
		});

		fileChooser = new PropertiesFileChooser();
		excelFileChooser = new ExcelFileChooser();

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
		add(tablePanel, BorderLayout.CENTER);
		add(textPanel, BorderLayout.SOUTH);
		setMinimumSize(new Dimension(500, 400));
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

}
