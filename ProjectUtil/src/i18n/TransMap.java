package i18n;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class TransMap implements Comparable<TransMap> {
	private String key;
	private Map<LanguageCode, String> translations;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Map<LanguageCode, String> getTranslations() {
		return translations;
	}

	public void setTranslations(Map<LanguageCode, String> translations) {
		this.translations = translations;
	}

	public void setTranslationsByLanguageCode(LanguageCode code,
			String translation) {
		translations.put(code, translation);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransMap other = (TransMap) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public int compareTo(TransMap o) {
		return this.key.compareTo(o.key);
	}

	@Override
	public String toString() {
		
		StringBuilder message = new StringBuilder();
		
		Iterator<Entry<LanguageCode, String>> it = translations.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<LanguageCode, String> record = it.next();
			message.append( record.getKey()+"\t="+record.getValue() +"\n");
		}
		
		return "key\t=" + key + "\n"+ message;
	}

	
}
