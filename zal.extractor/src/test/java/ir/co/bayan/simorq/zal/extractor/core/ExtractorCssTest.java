package ir.co.bayan.simorq.zal.extractor.core;

import static org.junit.Assert.*;
import ir.co.bayan.simorq.zal.extractor.core.ExtractedDoc.LinkData;
import ir.co.bayan.simorq.zal.extractor.model.ExtractorConfig;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractorCssTest {

	private static ExtractEngine extractEngine;
	private static byte[] testPageContent;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InputStreamReader configReader = new InputStreamReader(
				ExtractorCssTest.class.getResourceAsStream("/extractors-css-test.xml"));
		ExtractorConfig extractorConfig = ExtractorConfig.readConfig(configReader);
		extractEngine = new ExtractEngine(extractorConfig);
		InputStream testPage = ExtractorCssTest.class.getResourceAsStream("/test.htm");
		testPageContent = IOUtils.toByteArray(testPage);
	}

	@Test
	public void testValues() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);
		Map<String, String> result = extractedDocs.get(0).getFields();

		assertEquals("t1", result.get("f1"));
		assertEquals("t2", result.get("f2"));
		assertEquals("t3", result.get("f3"));
		assertEquals("http://some.blog.ir", result.get("f4"));
		assertEquals("t1 t2 t3 http://some.blog.ir", result.get("f5"));
	}

	@Test
	public void testSub() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);
		Map<String, String> result = extractedDocs.get(0).getFields();

		assertEquals("2-t", result.get("f6"));
		assertEquals("2012-12-19", result.get("f6.1"));
	}

	@Test
	public void testSelect() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);
		Map<String, String> result = extractedDocs.get(0).getFields();

		assertEquals("a b", result.get("f7"));
		assertEquals("a", result.get("f8"));
		assertEquals("2", result.get("f9"));
		assertEquals("b", result.get("f10"));
	}

	@Test
	public void testInheritence() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir2"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);
		Map<String, String> result = extractedDocs.get(0).getFields();

		assertEquals("t1", result.get("f1"));
		assertEquals("t1", result.get("f11"));
	}

	@Test
	public void testType() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir2"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);
		Map<String, String> result = extractedDocs.get(0).getFields();

		assertEquals("world!", result.get("f12"));
	}

	@Test
	public void testMultiDoc() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir3"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);
		assertEquals(3, extractedDocs.size());

		assertEquals("a", extractedDocs.get(1).getFields().get("content"));
		assertEquals("http://1", extractedDocs.get(1).getUrl());

		assertEquals("b", extractedDocs.get(2).getFields().get("content"));
		assertEquals("http://2", extractedDocs.get(2).getUrl());
	}

	@Test
	public void testOutlinks() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir4"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);
		List<LinkData> outlinks = extractedDocs.get(0).getOutlinks();
		assertEquals(2, outlinks.size());
		assertEquals("http://1", outlinks.get(0).getUrl());
		assertEquals("a", outlinks.get(0).getAnchor());
		assertEquals("http://some.blog.ir4/2", outlinks.get(1).getUrl());
	}

	@Test
	public void testCopy() throws Exception {
		Content content = new Content(new URL("http://some.blog.ir"), testPageContent, "UTF-8", "text/html");
		List<ExtractedDoc> extractedDocs = extractEngine.extract(content);

		Map<String, String> result = extractedDocs.get(0).getFields();

		assertEquals("t1 t2", result.get("content"));
	}
}
