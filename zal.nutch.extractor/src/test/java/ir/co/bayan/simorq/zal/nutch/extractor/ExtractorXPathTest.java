package ir.co.bayan.simorq.zal.nutch.extractor;

import static org.junit.Assert.assertEquals;
import ir.co.bayan.simorq.zal.nutch.extractor.config.SelectorConfiguration;
import ir.co.bayan.simorq.zal.nutch.extractor.engine.XPathEngine;

import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractorXPathTest {

	private static Extractor extractor;
	private static String encoding = "UTF-8";

	@BeforeClass
	public static void init() throws Exception {
		SelectorConfiguration config = SelectorConfiguration.readConfig(new InputStreamReader(XPathEngine.class
				.getResourceAsStream("/extractors-xpath-test.xml")));
		extractor = new Extractor(config);
	}

	@Test
	public void testExtractDocuments() throws Exception {
		byte[] content = IOUtils.toByteArray(XPathEngine.class.getResourceAsStream("/test.xml"));
		List<ExtractedDoc> docs = extractor.extract("http://a.blog.ir", content, encoding, "");
		ExtractedDoc doc = docs.get(0);
		assertEquals("content1", doc.getFields().get("f1"));
		assertEquals("b1-b3", doc.getFields().get("f2"));
		assertEquals("", doc.getFields().get("f3"));
		assertEquals("", doc.getFields().get("f4"));
		assertEquals("1", doc.getFields().get("f5"));
	}

	@Test
	public void testExtractDocumentsNamespace() throws Exception {
		byte[] content = IOUtils.toByteArray(XPathEngine.class.getResourceAsStream("/test-ns.xml"));
		List<ExtractedDoc> docs = extractor.extract("http://a.blog.ir", content, encoding, "");
		ExtractedDoc doc = docs.get(0);

		assertEquals("", doc.getFields().get("f1"));
		assertEquals("", doc.getFields().get("f2"));
		assertEquals("content1", doc.getFields().get("f3"));
		assertEquals("content3", doc.getFields().get("f4"));
		assertEquals("", doc.getFields().get("f5"));
	}

	@Test
	public void testMultiDoc() throws Exception {
		byte[] content = IOUtils.toByteArray(XPathEngine.class.getResourceAsStream("/test.xml"));
		List<ExtractedDoc> docs = extractor.extract("http://a.blog.ir2", content, encoding, "");

		assertEquals(2, docs.size());
		ExtractedDoc doc = docs.get(0);
		assertEquals("c1", doc.getUrl());
		assertEquals("content1 content2 content3", doc.getFields().get("content"));
	}
}
