package model.i18n;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Database {
	List<TransMap> transMaps;

	public Database() {
		transMaps = new LinkedList<TransMap>();
	}

	public List<TransMap> getTransMaps() {
		return Collections.unmodifiableList(transMaps);
	}

	public void setTransMap(List<TransMap> transMaps) {
		this.transMaps = transMaps;
	}

	public void addAll(List<TransMap> maps) {
		transMaps.addAll(maps);
	}

	public void clear() {
		transMaps.clear();
	}

	public void removenTransMap(int index) {
		transMaps.remove(index);
	}
}
