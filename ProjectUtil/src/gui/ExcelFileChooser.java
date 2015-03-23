package gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class ExcelFileChooser extends JFileChooser {

	public ExcelFileChooser() {
		setCurrentDirectory(new File(Utils.DEFAULT_FOLDER));
		addChoosableFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "Excel (*.xlsx)";
			}

			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				}
				String name = f.getName();

				String extension = Utils.getExtension(name);
				if (extension == null) {
					return false;
				}

				if ("xlsx".equals(extension)) {
					return true;
				}
				return false;
			}
		});
	}
}
