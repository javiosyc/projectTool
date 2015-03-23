package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.i18n.LanguageCode;
import model.i18n.TransMap;

public class TranslationModel extends AbstractTableModel {

	List<TransMap> db;

	String[] colNames = { "No.","KEY", "繁中", "簡中", "英文", "越南" };

	public TranslationModel() {
	}
	@Override
	public int getRowCount() {
		return db.size();
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		
		TransMap transMap = db.get(rowIndex);
		
		switch (columnIndex){
		case 0:
			return rowIndex+1;
		case 1:
			return transMap.getKey(); 
		case 2:
			return transMap.getTranslations().get(LanguageCode.zh_TW);
		case 3:
			return transMap.getTranslations().get(LanguageCode.zh_CN);
		case 4:
			return transMap.getTranslations().get(LanguageCode.en);
		case 5:
			return transMap.getTranslations().get(LanguageCode.vn);
		}
		
		return null;
	}

	public void setData(List<TransMap> db) {
		this.db = db;
	}

	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}

}
