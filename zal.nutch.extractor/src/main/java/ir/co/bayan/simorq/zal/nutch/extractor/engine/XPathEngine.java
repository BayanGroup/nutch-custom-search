package ir.co.bayan.simorq.zal.nutch.extractor.engine;

import ir.co.bayan.simorq.zal.nutch.extractor.config.Document;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Partition;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
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
	public Object evaluate(String value, XPathContext context) throws Exception {
		XPath xPath = xPathFactory.newXPath();
		if (context.getNsContext() != null)
			xPath.setNamespaceContext(context.getNsContext());
		return xPath.compile(value).evaluate(context.getRoot(), XPathConstants.NODESET);
	}

	@Override
	public Object getAttribute(Object res, String name, XPathContext context) throws Exception {
		if (res instanceof NodeList) {
			NodeList nodes = (NodeList) res;
			List<String> texts = new ArrayList<>(nodes.getLength());
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node instanceof Element)
					texts.add(((Element) node).getAttribute(name));
			}
			return texts;
		} else
			return ((Element) res).getAttribute(name);
	}

	@Override
	public Object getText(Object res, XPathContext context) throws Exception {
		if (res instanceof NodeList) {
			NodeList nodes = (NodeList) res;
			List<String> texts = new ArrayList<>(nodes.getLength());
			for (int i = 0; i < nodes.getLength(); i++)
				texts.add(nodes.item(i).getTextContent());
			return texts;
		} else
			return ((Node) res).getTextContent();
	}

	@Override
	protected XPathContext createContext(String url, byte[] content, String encoding, String contentType)
			throws Exception {
		Reader contentReader = new InputStreamReader(new ByteArrayInputStream(content), encoding);
		Element root = builder.parse(new InputSource(contentReader)).getDocumentElement();
		NamespaceContext nsContext = getNamespaceContext(root);
		return new XPathContext(this, url, root, nsContext);
	}

	@Override
	protected List<?> getRoots(Document document, XPathContext context) throws Exception {
		Partition partition = document.getPartition();
		if (partition == null) {
			return Arrays.asList(context.getRoot());
		} else {
			NodeList nodes = (NodeList) partition.getExpr().extract(context);
			List<Node> res = new ArrayList<>(nodes.getLength());
			for (int i = 0; i < nodes.getLength(); i++) {
				res.add(nodes.item(i));
			}
			return res;
		}
	}

}
