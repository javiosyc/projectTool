package gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class CSVFileFilter extends FileFilter {

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

		if ("csv".equals(extension) || "txt".equals(extension)) {
			return true;
		}

		return false;
	}

	@Override
	public String getDescription() {
		return "CSV (*.csv)";
	}

}
