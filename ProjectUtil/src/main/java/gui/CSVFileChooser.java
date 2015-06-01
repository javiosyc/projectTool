package gui;

import java.io.File;

import javax.swing.JFileChooser;

public class CSVFileChooser extends JFileChooser {

	private static final long serialVersionUID = -1129149256930070555L;

	public CSVFileChooser(String dir) {
		setCurrentDirectory(new File(dir));
		setAcceptAllFileFilterUsed(false);
		addChoosableFileFilter(new CSVFileFilter());
	}
}
