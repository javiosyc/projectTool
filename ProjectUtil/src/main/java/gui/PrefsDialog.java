package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class PrefsDialog extends JDialog {

	private static final long serialVersionUID = -4072445341490010759L;

	private JButton okButton;

	private JButton cancelButton;

	private JButton resetButton;

	private JButton selectImportButton;

	private JFileChooser fileChooser;

	private JTextField defaultImportPathField;

	private PrefsListener prefsListener;

	private JTextField defaultExportPathField;
	private JButton selectExportButton;

	public PrefsDialog(JFrame parent) {
		super(parent, "Preferences", false);

		okButton = new JButton("Ok");
		cancelButton = new JButton("Cancel");
		resetButton = new JButton("Reset");

		defaultImportPathField = new JTextField(30);
		selectImportButton = new JButton("Import Path");

		defaultExportPathField = new JTextField(30);
		selectExportButton = new JButton("Export Path");

		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);

		setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();

		gc.gridy = 0;

		// First row
		gc.weightx = 1;
		gc.weighty = 1;

		gc.fill = GridBagConstraints.NONE;
		gc.gridx = 0;
		add(selectImportButton, gc);

		gc.gridx++;
		gc.gridwidth = 2;
		add(defaultImportPathField, gc);

		// / Next row

		gc.gridy++;
		gc.gridwidth = 1;
		gc.gridx = 0;

		add(selectExportButton, gc);

		gc.gridx++;
		gc.gridwidth = 2;

		add(defaultExportPathField, gc);

		// Next row
		gc.gridy++;
		gc.gridwidth = 1;
		gc.gridx = 0;
		add(okButton, gc);

		gc.gridx++;
		gc.gridwidth = 1;
		add(cancelButton, gc);

		gc.gridx++;
		add(resetButton, gc);

		selectImportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (prefsListener != null) {
					String path = prefsListener.getDefaultImporPath();
					fileChooser.setCurrentDirectory(new File(path));
				}

				if (fileChooser.showOpenDialog(PrefsDialog.this) == JFileChooser.APPROVE_OPTION) {
					File path = fileChooser.getSelectedFile();
					defaultImportPathField.setText(path.toString()
							+ File.separator);
				}
			}
		});

		selectExportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (prefsListener != null) {
					String path = prefsListener.getDefaultExporPath();
					fileChooser.setCurrentDirectory(new File(path));
				}

				if (fileChooser.showOpenDialog(PrefsDialog.this) == JFileChooser.APPROVE_OPTION) {
					File path = fileChooser.getSelectedFile();
					defaultExportPathField.setText(path.toString()
							+ File.separator);
				}
			}
		});

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String defaultImportPath = defaultImportPathField.getText();
				String defalutExportPath = defaultExportPathField.getText();
				if (prefsListener != null) {
					prefsListener.setPreferences(defaultImportPath,
							defalutExportPath);
				}
				setVisible(false);
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (prefsListener != null) {
					
					defaultImportPathField.setText(prefsListener.getDefaultImporPath());
					defaultExportPathField.setText(prefsListener.getDefaultExporPath());
				}
			}
		});

		setSize(600, 300);

		setLocationRelativeTo(parent);
	}

	public void setDefaultImportPath(String defaultImportPathField) {
		this.defaultImportPathField.setText(defaultImportPathField);
	}

	public void setPrefsListener(PrefsListener prefsListener) {
		this.prefsListener = prefsListener;
	}

	public void setDefaultExportPath(String defaultExportPath) {
		this.defaultExportPathField.setText(defaultExportPath);
	}

}
