package gui;


public interface PrefsListener {

	void setPreferences(String defaultImportPath,
			String defaultExportPath);

	String getDefaultExporPath();

	String getDefaultImporPath();
}
