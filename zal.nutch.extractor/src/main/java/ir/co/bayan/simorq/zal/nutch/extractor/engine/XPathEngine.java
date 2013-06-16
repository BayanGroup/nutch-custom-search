package ir.co.bayan.simorq.zal.nutch.extractor.engine;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
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
public class XPathEngine extends ExtractEngine<XPathContext> {

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

	@Override
	public List<?> evaluate(String value, XPathContext context) throws Exception {
		XPath xPath = xPathFactory.newXPath();
		if (context.getNsContext() != null)
			xPath.setNamespaceContext(context.getNsContext());
		NodeList nodeList = (NodeList) xPath.compile(value).evaluate(context.getRoot(), XPathConstants.NODESET);
		List<Node> list = new ArrayList<>(nodeList.getLength());
		for (int i = 0; i < nodeList.getLength(); i++) {
			list.add(nodeList.item(i));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> getAttribute(List<?> res, String name, XPathContext context) throws Exception {
		List<Node> nodes = (List<Node>) res;
		List<String> texts = new ArrayList<>(nodes.size());
		for (Node node : nodes)
			if (node instanceof Element)
				texts.add(((Element) node).getAttribute(name));
		return texts;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> getText(List<?> res, XPathContext context) throws Exception {
		List<Node> nodes = (List<Node>) res;
		List<String> texts = new ArrayList<>(nodes.size());
		for (Node node : nodes)
			texts.add(node.getTextContent());
		return texts;
	}

	@Override
	protected XPathContext createContext(String url, byte[] content, String encoding, String contentType)
			throws Exception {
		Reader contentReader = new InputStreamReader(new ByteArrayInputStream(content), encoding);
		Element root = builder.parse(new InputSource(contentReader)).getDocumentElement();
		NamespaceContext nsContext = getNamespaceContext(root);
		return new XPathContext(this, url, root, nsContext);
	}

}
