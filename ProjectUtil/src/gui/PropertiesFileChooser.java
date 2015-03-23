package gui;

import java.io.File;

import javax.swing.JFileChooser;

public class PropertiesFileChooser extends JFileChooser {

	public PropertiesFileChooser() {
		setCurrentDirectory(new File(Utils.DEFAULT_FOLDER));
		setAcceptAllFileFilterUsed(false);
		setMultiSelectionEnabled(true);
		addChoosableFileFilter(new PropertiesFileFilter());
	}
}
