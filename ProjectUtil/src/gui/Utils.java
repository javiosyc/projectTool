package gui;

import java.io.File;

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

}
