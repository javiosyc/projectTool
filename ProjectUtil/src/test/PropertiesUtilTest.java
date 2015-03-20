package test;

import i18n.PropertiestUtil;
import i18n.TransMap;

import java.io.IOException;
import java.util.TreeSet;

import org.junit.Test;

public class PropertiesUtilTest {

	@Test
	public void test() throws IOException {
		PropertiestUtil util = new PropertiestUtil();

		String[] args = { "test/resource/ApplicationResource_zh_TW.properties",
				"test/resource/ApplicationResource_zh_CN.properties",
				"test/resource/ApplicationResource_en.properties",
				"test/resource/ApplicationResource_vn.properties" };

		TreeSet<TransMap> result = util.getPropValues(args);

		util.exportData("src/test/out/testExport.xlsx", result);

	}

}
