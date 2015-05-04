package gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class ExcelFileChooser extends JFileChooser {

	private static final long serialVersionUID = -2630021070509809637L;

	public ExcelFileChooser(String dir) {
		setCurrentDirectory(new File(dir));
		addChoosableFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "Excel (*.xlsx)";
			}

			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					return false;
				}
			}
		});
	}
}
