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

	private JButton okButton;

	private JButton cancelButton;

	private JButton resetButton;

	private JButton selectFileButton;

	private JFileChooser fileChooser;

	private JTextField defaultPathField;

	private PrefsListener prefsListener;

	public PrefsDialog(JFrame parent) {
		super(parent, "Preferences", false);

		okButton = new JButton("Ok");
		cancelButton = new JButton("Cancel");
		resetButton = new JButton("Reset");
		defaultPathField = new JTextField(20);

		selectFileButton = new JButton("select path");

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
		add(selectFileButton, gc);

		gc.gridy++;
		gc.gridwidth = 2;
		add(defaultPathField, gc);
		// / Next row

		gc.gridy++;
		gc.gridwidth = 1;
		gc.gridx = 0;

		add(okButton, gc);

		gc.gridx++;
		add(cancelButton, gc);

		gc.gridy++;

		gc.gridx = 0;
		add(resetButton, gc);
		selectFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showOpenDialog(PrefsDialog.this) == JFileChooser.APPROVE_OPTION) {
					File path = fileChooser.getSelectedFile();
					defaultPathField.setText(path.toString() + File.separator);
				}
			}
		});

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String defaultPath = defaultPathField.getText();

				if (prefsListener != null) {
					prefsListener.preferencesSet(defaultPath);
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
					prefsListener.preferenceReset();
				}
			}
		});

		setSize(400, 300);

		setLocationRelativeTo(parent);
	}

	public void setDefaulsts(String defalutPath) {
		defaultPathField.setText(defalutPath);
	}

	public void setPrefsListener(PrefsListener prefsListener) {
		this.prefsListener = prefsListener;
	}

}
