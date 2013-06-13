package ir.co.bayan.simorq.zal.nutch.extractor.engine;

import ir.co.bayan.simorq.zal.nutch.extractor.ExtractedDoc;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Document;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class XPathEngine extends ExtractEngine {

	public static final String DEFAULT_NAMESPACE = "dns";
	private static final String XMLNS = "xmlns";

	private final DocumentBuilder builder;
	private final XPathFactory xPathFactory;

	public XPathEngine() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		builder = factory.newDocumentBuilder();
		xPathFactory = XPathFactory.newInstance();
	}

	@Override
	public List<ExtractedDoc> extractDocuments(Document document, String url, byte[] content, String encoding,
			String contentType) throws Exception {
		Reader contentReader = new InputStreamReader(new ByteArrayInputStream(content), encoding);
		Element root = builder.parse(new InputSource(contentReader)).getDocumentElement();
		NamespaceContext nsContext = getNamespaceContext(root);

		List<ExtractedDoc> result = new ArrayList<>();
		if (document.getPartitionBy() != null) {
			XPath partitionXPath = xPathFactory.newXPath();
			if (nsContext != null)
				partitionXPath.setNamespaceContext(nsContext);
			XPathExpression xpath = partitionXPath.compile(document.getPartitionBy());
			NodeList documents = (NodeList) xpath.evaluate(root, XPathConstants.NODESET);
			for (int i = 0; i < documents.getLength(); i++) {
				Node item = documents.item(i);
				if (item instanceof Element) {
					ExtractedDoc extractedDoc = extractDocument(document, url, (Element) item, nsContext);
					String id = extractedDoc.getFields().remove("id");
					extractedDoc.setUrl(id);
					result.add(extractedDoc);
				}
			}
		} else {
			ExtractedDoc extractedDoc = extractDocument(document, url, root, nsContext);
			result.add(extractedDoc);
		}
		return result;
	}

	private ExtractedDoc extractDocument(Document document, String url, Element root, NamespaceContext nsContext)
			throws Exception {
		XPathContext context = new XPathContext(url, root, nsContext);
		ExtractedDoc extractedDoc = new ExtractedDoc(new HashMap<String, String>(
				document.getExtractTos().size() * 2 + 1), url);
		extractDocument(document, extractedDoc, context);
		return extractedDoc;
	}

	protected NamespaceContext getNamespaceContext(Element root) {
		NamespaceContext nsContext = null;
		MapNamespaceContext mapNs = new MapNamespaceContext();
		NamedNodeMap namedNodeMap = root.getAttributes();

		for (int i = 0; i < namedNodeMap.getLength(); i++) {
			Node item = namedNodeMap.item(i);
			String nodeName = item.getNodeName();
			if (nodeName.startsWith(XMLNS)) {
				if (XMLNS.equals(nodeName)) {
					mapNs.addNamespace(DEFAULT_NAMESPACE, item.getNodeValue());
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
