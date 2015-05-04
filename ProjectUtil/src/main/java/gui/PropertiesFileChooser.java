package gui;

import java.io.File;

import javax.swing.JFileChooser;

public class PropertiesFileChooser extends JFileChooser {

	private static final long serialVersionUID = -1129149256930070555L;

	public PropertiesFileChooser(String dir) {
		setCurrentDirectory(new File(dir));
		setAcceptAllFileFilterUsed(false);
		setMultiSelectionEnabled(true);
		addChoosableFileFilter(new PropertiesFileFilter());
	}
}
