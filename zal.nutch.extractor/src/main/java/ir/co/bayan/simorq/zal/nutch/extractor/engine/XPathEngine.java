package ir.co.bayan.simorq.zal.nutch.extractor.engine;

import ir.co.bayan.simorq.zal.nutch.extractor.ExtractedDoc;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Document;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
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
		Element root = parsedDoc.getDocumentElement();

		NamespaceContext nsContext = getNamespaceContext(root);

		XPathContext context = new XPathContext(url, root, nsContext);
		ExtractedDoc extractedDoc = new ExtractedDoc(new HashMap<String, String>(
				document.getExtractTos().size() * 2 + 1), url);
		extractDocument(document, extractedDoc, context);
		return Arrays.asList(extractedDoc);
	}

	private NamespaceContext getNamespaceContext(Element root) {
		NamespaceContext nsContext = null;
		MapNamespaceContext mapNs = new MapNamespaceContext();
		NamedNodeMap namedNodeMap = root.getAttributes();

		for (int i = 0; i < namedNodeMap.getLength(); i++) {
			Node item = namedNodeMap.item(i);
			String nodeName = item.getNodeName();
			if (nodeName.startsWith("xmlns")) {
				if ("xmlns".equals(nodeName)) {
					mapNs.addNamespace("dns", item.getNodeValue());
				} else {
					nodeName = StringUtils.removeStart(nodeName, "xmlns:");
					mapNs.addNamespace(nodeName, item.getNodeValue());
				}
			}
		}
		if (!mapNs.getNamespaces().isEmpty()) {
			nsContext = mapNs;
		}
		return nsContext;
	}
}
