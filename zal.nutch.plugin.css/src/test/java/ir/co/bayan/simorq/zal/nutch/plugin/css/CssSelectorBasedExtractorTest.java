package ir.co.bayan.simorq.zal.nutch.plugin.css;

import static org.junit.Assert.assertEquals;
import ir.co.bayan.simorq.zal.nutch.plugin.css.config.SelectorConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class CssSelectorBasedExtractorTest {

	private static CssSelectorBasedExtractor extractor;
	private static String testPageContent;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InputStreamReader configReader = new InputStreamReader(
				CssSelectorBasedExtractorTest.class.getResourceAsStream("/css-paths-selector-test.xml"));
		SelectorConfiguration config = SelectorConfiguration.readConfig(configReader);
		extractor = new CssSelectorBasedExtractor(config);
		InputStream testPage = CssSelectorBasedExtractorTest.class.getResourceAsStream("/test.htm");
		testPageContent = IOUtils.toString(testPage, "UTF-8");
	}

	@Test
	public void testValues() throws IOException {
		Map<String, String> result = extractor.extract("http://some.blog.ir", testPageContent);

		assertEquals("t1", result.get("f1"));
		assertEquals("t2", result.get("f2"));
		assertEquals("t3", result.get("f3"));
		assertEquals("http://some.blog.ir", result.get("f4"));
		assertEquals("t1 t2 t3 http://some.blog.ir", result.get("f5"));
	}

	@Test
	public void testSub() throws IOException {
		Map<String, String> result = extractor.extract("http://some.blog.ir", testPageContent);
		assertEquals("2-t", result.get("f6"));
		assertEquals("2012-12-19", result.get("f6.1"));
	}

	@Test
	public void testSelect() throws IOException {
		Map<String, String> result = extractor.extract("http://some.blog.ir", testPageContent);

		assertEquals("a b", result.get("f7"));
		assertEquals("a", result.get("f8"));
		assertEquals("2", result.get("f9"));
		assertEquals("b", result.get("f10"));
	}

	@Test
	public void testInheritence() throws IOException {
		Map<String, String> result = extractor.extract("http://some.blog.ir2", testPageContent);

		assertEquals("t1", result.get("f1"));
		assertEquals("t1", result.get("f11"));
	}

	@Test
	public void testType() throws IOException {
		Map<String, String> result = extractor.extract("http://some.blog.ir2", testPageContent);

		assertEquals("world!", result.get("f12"));
	}
}
