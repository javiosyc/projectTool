package model.i18n;

public enum LanguageCode {
	en, vn, zh_CN, zh_TW;

	public static final String EXTENSION = ".properties";

	public static LanguageCode parsePath(String path) {

		if (path.endsWith("_zh_TW" + EXTENSION)) {
			return zh_TW;
		} else if (path.endsWith("_zh_CN" + EXTENSION)) {
			return zh_CN;
		} else if (path.endsWith("_vn" + EXTENSION)) {
			return vn;
		} else if (path.endsWith("_en" + EXTENSION)) {
			return en;
		}

		return null;

	}
}
