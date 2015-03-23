package test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import model.i18n.LanguageCode;
import model.i18n.PropertiestUtil;
import model.i18n.TransMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Test;

public class PropertiesUtilTest {

	public void test() throws IOException {
		PropertiestUtil util = new PropertiestUtil();

		String[] args = { "test/resource/ApplicationResource_zh_TW.properties",
				"test/resource/ApplicationResource_zh_CN.properties",
				"test/resource/ApplicationResource_en.properties",
				"test/resource/ApplicationResource_vn.properties" };

		List<TransMap> result = util.getPropValues(args);

		//util.exportData(new File("src/test/out/testExport.xlsx"), result);

	}

	@Test
	public void singleFileTest() {
		PropertiestUtil util = new PropertiestUtil();
		String[] args = { "test/resource/test_zh_TW.properties" };

		try {
			List<TransMap> result = util.getPropValues(args);

			assertTrue(result.size() == 1);

			TransMap record = result.get(0);

			assertTrue("check TransMap key failed!",
					"test".equals(record.getKey()));

			Map<LanguageCode, String> translation = record.getTranslations();

			assertTrue("check translations size failed!",
					translation.size() == 1);

			assertTrue("check translations value failed!",
					"測試".equals(translation.get(LanguageCode.zh_TW)));

		} catch (IOException e) {
			String message = ExceptionUtils.getMessage(e);
			fail(message);
		}

	}
}
