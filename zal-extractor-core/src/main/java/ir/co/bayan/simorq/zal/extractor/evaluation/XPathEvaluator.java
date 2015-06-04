package ir.co.bayan.simorq.zal.extractor.evaluation;

import ir.co.bayan.simorq.zal.extractor.core.Content;
import ir.co.bayan.simorq.zal.extractor.core.ExtractUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.cyberneko.html.filters.DefaultFilter;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of ExtractEngine which uses JAXP xpath engine to evaluate expressions. It also handles the
 * namespaces. If the given xml has a default namespace, it can be accessed in the xpath expression by prefix dns
 * (Default Name Space).
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class XPathEvaluator implements Evaluator<XPathContext> {

	public static final String DEFAULT_NAMESPACE = "dns";
	private static final String XMLNS = "xmlns";

	private final DocumentBuilder builder;
	private final XPathFactory xPathFactory;
	private final boolean namespaceAware;

	public XPathEvaluator(boolean namespaceAware) {
		this.namespaceAware = namespaceAware;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(namespaceAware);
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
		xPathFactory = XPathFactory.newInstance();
	}

	protected NamespaceContext getNamespaceContext(Element root) {
		if(!namespaceAware)
			return null;
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
	public List<?> evaluate(Object root, XPathContext context, String value) throws Exception {
		XPath xPath = xPathFactory.newXPath();
		if (context.getNsContext() != null)
			xPath.setNamespaceContext(context.getNsContext());
		NodeList nodeList = (NodeList) xPath.compile(value).evaluate(root, XPathConstants.NODESET);
		List<Node> list = new ArrayList<Node>(nodeList.getLength());
		for (int i = 0; i < nodeList.getLength(); i++) {
			list.add(nodeList.item(i));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> getAttribute(XPathContext context, List<?> res, String name) throws Exception {
		List<Node> nodes = (List<Node>) res;
		List<String> texts = new ArrayList<String>(nodes.size());
		String localName;
		String ns;
		String[] parts = name.split(":");
		if (parts.length == 2) {
			ns = context.getNsContext().getNamespaceURI(parts[0]);
			localName = parts[1];
		} else {
			ns = null;
			localName = parts[0];
		}
		for (Node node : nodes) {
			if (node instanceof Element) {
				String attrValue = ((Element) node).getAttributeNS(ns, localName);
				texts.add(attrValue);
			}
		}
		return texts;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> getText(XPathContext context, List<?> input) throws Exception {
		List<Node> nodes = (List<Node>) input;
		List<String> texts = new ArrayList<String>(nodes.size());
		for (Node node : nodes) {
			texts.add(node.getTextContent());
		}
		return texts;
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<?> getRaw(XPathContext context, List<?> input) throws Exception {
        List<Node> nodes = (List<Node>) input;
        List<String> texts = new ArrayList<String>(nodes.size());
        for (Node node : nodes) {
            texts.add(node.getNodeValue());
        }
        return texts;
    }

	@Override
	public XPathContext createContext(Content content) throws Exception {
		InputSource is = new InputSource(content.getData());
		is.setEncoding(content.getEncoding());
		Element root = null;
		if (ExtractUtil.isHtml(content.getType())) {
			DOMParser parser = new DOMParser();
			parser.setFeature("http://xml.org/sax/features/namespaces", namespaceAware);
			parser.setProperty("http://cyberneko.org/html/properties/default-encoding", content.getEncoding());
			parser.setFeature("http://cyberneko.org/html/features/scanner/ignore-specified-charset", true);
			parser.setFeature("http://cyberneko.org/html/features/balance-tags/ignore-outside-content", false);
			parser.setFeature("http://cyberneko.org/html/features/scanner/allow-selfclosing-tags", true);
			parser.setProperty("http://cyberneko.org/html/properties/names/elems", "match");
			parser.setProperty("http://cyberneko.org/html/properties/names/attrs", "no-change");

			// There is a problem with namespaces as described in http://nekohtml.sourceforge.net/faq.html
			// parser.setFeature("http://xml.org/sax/features/namespaces", false);
			parser.setProperty("http://cyberneko.org/html/properties/filters",
					new XMLDocumentFilter[] { new DefaultFilter() {
						@Override
						public void startElement(QName element, XMLAttributes attrs, Augmentations augs)
								throws XNIException {
							element.uri = null;
							super.startElement(element, attrs, augs);
						}
					} });
			parser.parse(is);
			root = parser.getDocument().getDocumentElement();
		} else {
			root = builder.parse(is).getDocumentElement();
		}
		if (root != null) {
			NamespaceContext nsContext = getNamespaceContext(root);
			return new XPathContext(this, content, root, nsContext);
		}
		throw new RuntimeException("Only html or xml is valid in XPathEngine but the given content type is "
				+ content.getType());
	}

	@Override
	public String getName() {
		return "xpath";
	}

}
