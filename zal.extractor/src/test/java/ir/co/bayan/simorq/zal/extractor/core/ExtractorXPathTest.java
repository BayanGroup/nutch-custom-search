package ir.co.bayan.simorq.zal.extractor.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import ir.co.bayan.simorq.zal.extractor.core.ExtractedDoc.LinkData;
import ir.co.bayan.simorq.zal.extractor.evaluation.XPathEvaluator;
import ir.co.bayan.simorq.zal.extractor.model.ExtractorConfig;

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

	private static ExtractEngine extractEngine;
	private static String encoding = "UTF-8";

	@BeforeClass
	public static void init() throws Exception {
		ExtractorConfig extractorConfig = ExtractorConfig.readConfig(new InputStreamReader(XPathEvaluator.class
				.getResourceAsStream("/extractors-xpath-test.xml")));
		extractEngine = new ExtractEngine(extractorConfig);
	}

	@Test
	public void testExtractDocuments() throws Exception {
		byte[] content = IOUtils.toByteArray(XPathEvaluator.class.getResourceAsStream("/test.xml"));
		List<ExtractedDoc> docs = extractEngine.extract("http://a.blog.ir", content, encoding, "+xml");
		ExtractedDoc doc = docs.get(0);
		assertEquals("content1", doc.getFields().get("f1"));
		assertEquals("b1-b3", doc.getFields().get("f2"));
		assertEquals("", doc.getFields().get("f3"));
		assertEquals("", doc.getFields().get("f4"));
		assertEquals("1", doc.getFields().get("f5"));
	}

	@Test
	public void testExtractDocumentsNamespace() throws Exception {
		byte[] content = IOUtils.toByteArray(XPathEvaluator.class.getResourceAsStream("/test-ns.xml"));
		List<ExtractedDoc> docs = extractEngine.extract("http://a.blog.ir", content, encoding, "+xml");
		ExtractedDoc doc = docs.get(0);

		assertEquals("", doc.getFields().get("f1"));
		assertEquals("", doc.getFields().get("f2"));
		assertEquals("content1", doc.getFields().get("f3"));
		assertEquals("content3", doc.getFields().get("f4"));
		assertEquals("", doc.getFields().get("f5"));
	}

	@Test
	public void testMultiDoc() throws Exception {
		byte[] content = IOUtils.toByteArray(XPathEvaluator.class.getResourceAsStream("/test.xml"));
		List<ExtractedDoc> docs = extractEngine.extract("http://a.blog.ir2", content, encoding, "+xml");

		assertEquals(3, docs.size());
		ExtractedDoc doc = docs.get(1);
		assertEquals("c1", doc.getUrl());
		assertEquals("content1 content2 content3", doc.getFields().get("content"));
	}

	@Test
	public void testMultiDocInsideDoc() throws Exception {
		byte[] content = IOUtils.toByteArray(XPathEvaluator.class.getResourceAsStream("/test.xml"));
		List<ExtractedDoc> docs = extractEngine.extract("http://a.blog.ir25", content, encoding, "+xml");

		assertEquals(2, docs.size());
		ExtractedDoc doc = docs.get(0);
		assertEquals("c1", doc.getUrl());
		assertEquals("content1 content2 content3", doc.getFields().get("content"));
	}

	@Test
	public void testOutlinks() throws Exception {
		byte[] content = IOUtils.toByteArray(XPathEvaluator.class.getResourceAsStream("/test.xml"));
		List<ExtractedDoc> docs = extractEngine.extract("http://a.blog.ir3", content, encoding, "+xml");
		List<LinkData> outlinks = docs.get(0).getOutlinks();

		assertNotNull(outlinks);
		assertEquals(2, outlinks.size());
		assertEquals("http://a.blog.ir3/b3", outlinks.get(0).getUrl());
		assertEquals("content3", outlinks.get(0).getAnchor());

		assertEquals("http://a.blog.ir3/sitemap.xml", outlinks.get(1).getUrl());
	}
}
