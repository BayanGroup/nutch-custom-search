package ir.co.bayan.simorq.zal.nutch.extractor.engine;

import ir.co.bayan.simorq.zal.nutch.extractor.ExtractedDoc;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Document;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class XPathEngine extends ExtractEngine {

	private final DocumentBuilder builder;
	private final XPathFactory xPathFactory;

	public XPathEngine() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		builder = factory.newDocumentBuilder();
		xPathFactory = XPathFactory.newInstance();
	}

	public XPath getXPath() {
		return xPathFactory.newXPath();
	}

	@Override
	public List<ExtractedDoc> extractDocuments(Document document, String url, byte[] content, String encoding,
			String contentType) throws Exception {
		Reader contentReader = new InputStreamReader(new ByteArrayInputStream(content), encoding);
		org.w3c.dom.Document parsedDoc = builder.parse(new InputSource(contentReader));
		XPathContext context = new XPathContext(url, parsedDoc.getDocumentElement());
		ExtractedDoc extractedDoc = new ExtractedDoc(new HashMap<String, String>(
				document.getExtractTos().size() * 2 + 1), url);
		extractDocument(document, extractedDoc, context);
		return Arrays.asList(extractedDoc);
	}

}
