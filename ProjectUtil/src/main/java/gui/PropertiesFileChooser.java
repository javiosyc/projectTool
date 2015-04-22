package gui;

import java.io.File;

import javax.swing.JFileChooser;

public class PropertiesFileChooser extends JFileChooser {

	public PropertiesFileChooser(String dir) {
		setCurrentDirectory(new File(dir));
		setAcceptAllFileFilterUsed(false);
		setMultiSelectionEnabled(true);
		addChoosableFileFilter(new PropertiesFileFilter());
	}
}
