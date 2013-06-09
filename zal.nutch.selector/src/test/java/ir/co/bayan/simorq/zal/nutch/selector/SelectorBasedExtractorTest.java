package ir.co.bayan.simorq.zal.nutch.selector;

import static org.junit.Assert.assertEquals;
import ir.co.bayan.simorq.zal.nutch.selector.config.SelectorConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class SelectorBasedExtractorTest {

	private static SelectorBasedExtractor extractor;
	private static String testPageContent;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InputStreamReader configReader = new InputStreamReader(
				SelectorBasedExtractorTest.class.getResourceAsStream("/selectors-selector-test.xml"));
		SelectorConfiguration config = SelectorConfiguration.readConfig(configReader);
		extractor = new SelectorBasedExtractor(config);
		InputStream testPage = SelectorBasedExtractorTest.class.getResourceAsStream("/test.htm");
		testPageContent = IOUtils.toString(testPage, "UTF-8");
	}

	@Test
	public void testValues() throws IOException {
		List<ExtractedDoc> extractedDocs = extractor.extract("http://some.blog.ir", testPageContent, "text/html");
		Map<String, String> result = extractedDocs.get(0).getFields();

		assertEquals("t1", result.get("f1"));
		assertEquals("t2", result.get("f2"));
		assertEquals("t3", result.get("f3"));
		assertEquals("http://some.blog.ir", result.get("f4"));
		assertEquals("t1 t2 t3 http://some.blog.ir", result.get("f5"));
	}

	@Test
	public void testSub() throws IOException {
		List<ExtractedDoc> extractedDocs = extractor.extract("http://some.blog.ir", testPageContent, "text/html");
		Map<String, String> result = extractedDocs.get(0).getFields();

		assertEquals("2-t", result.get("f6"));
		assertEquals("2012-12-19", result.get("f6.1"));
	}

	@Test
	public void testSelect() throws IOException {
		List<ExtractedDoc> extractedDocs = extractor.extract("http://some.blog.ir", testPageContent, "text/html");
		Map<String, String> result = extractedDocs.get(0).getFields();

		assertEquals("a b", result.get("f7"));
		assertEquals("a", result.get("f8"));
		assertEquals("2", result.get("f9"));
		assertEquals("b", result.get("f10"));
	}

	@Test
	public void testInheritence() throws IOException {
		List<ExtractedDoc> extractedDocs = extractor.extract("http://some.blog.ir2", testPageContent, "text/html");
		Map<String, String> result = extractedDocs.get(0).getFields();

		assertEquals("t1", result.get("f1"));
		assertEquals("t1", result.get("f11"));
	}

	@Test
	public void testType() throws IOException {
		List<ExtractedDoc> extractedDocs = extractor.extract("http://some.blog.ir2", testPageContent, "text/html");
		Map<String, String> result = extractedDocs.get(0).getFields();

		assertEquals("world!", result.get("f12"));
	}

	@Test
	public void testMultiDoc() throws IOException {
		List<ExtractedDoc> extractedDocs = extractor.extract("http://some.blog.ir", testPageContent, "text/html");
	}
}
