package gui;

import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;

public class Utils {
	public static String DEFAULT_FOLDER = System.getProperty("user.dir")
			+ File.separator + "src" + File.separator;

	public static String getExtension(String name) {
		int pointIndex = name.lastIndexOf(".");
		if (pointIndex == -1) {
			return null;
		}

		if (pointIndex == name.length() - 1) {
			return null;
		}

		return name.substring(pointIndex + 1, name.length());
	}

	public static ImageIcon createIcon(String path) {
		URL url = System.class.getResource(path);

		if (url == null) {
			System.err.println("Unable to laod image:" + url);
			return null;
		}

		ImageIcon icon = new ImageIcon(url);
		return icon;
	}
}
