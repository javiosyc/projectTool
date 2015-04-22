package controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import model.i18n.Database;
import model.i18n.PropertiestUtil;
import model.i18n.TransMap;

public class Controller {
	Database db = new Database();

	public List<TransMap> getTransMaps() {
		return db.getTransMaps();
	}

	public void addAll(List<TransMap> maps) {
		db.addAll(maps);
	}

	public void clear() {
		db.clear();
	}

	public int loadFromFile(File[] selectedFile) throws IOException {
		PropertiestUtil util = new PropertiestUtil();
		List<TransMap> result = util.getPropValues(selectedFile);
		addAll(result);

		return result.size();
	}

	public void saveToFile(File file) {

		PropertiestUtil util = new PropertiestUtil();
		util.exportData(file, db.getTransMaps());
	}
	
	public void removenTransMap(int index) {
		db.removenTransMap(index);
	}
}
