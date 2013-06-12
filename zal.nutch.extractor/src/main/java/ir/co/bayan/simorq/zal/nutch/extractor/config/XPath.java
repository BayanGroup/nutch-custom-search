package ir.co.bayan.simorq.zal.nutch.extractor.config;

import ir.co.bayan.simorq.zal.nutch.extractor.engine.ExtractContext;
import ir.co.bayan.simorq.zal.nutch.extractor.engine.XPathContext;

import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement(name = "xpath")
public class XPath extends FieldValue {

	private static XPathFactory xPathFactory = XPathFactory.newInstance();

	private String expression;

	private XPathExpression xPathExpression;

	@XmlAttribute
	private final String delimiter = " ";

	private String pattern;

	private Pattern compiledPattern;

	@XmlAttribute
	private String substitution;

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern
	 *            the pattern to set
	 */
	@XmlAttribute
	public void setPattern(String pattern) {
		this.pattern = pattern;
		this.compiledPattern = Pattern.compile(pattern);
	}

	/**
	 * @param expression
	 *            the expression to set
	 * @throws XPathExpressionException
	 */
	@XmlAttribute
	public void setExpression(String expression) throws XPathExpressionException {
		this.expression = expression;
		xPathExpression = xPathFactory.newXPath().compile(expression);
	}

	/**
	 * @return the selector
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}

	@Override
	public void extract(ExtractContext eContext) throws Exception {
		XPathContext context = (XPathContext) eContext;
		if (context.getNsContext() != null) {
			javax.xml.xpath.XPath xPath = xPathFactory.newXPath();
			xPath.setNamespaceContext(context.getNsContext());
			xPathExpression = xPath.compile(expression);
		}

		StringBuilder tempRes;
		if (pattern == null) {
			tempRes = context.getResult();
		} else {
			tempRes = new StringBuilder();
		}

		NodeList nodeList = (NodeList) xPathExpression.evaluate(context.getRoot(), XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			switch (node.getNodeType()) {
			case Node.ATTRIBUTE_NODE:
				Attr attr = (Attr) node;
				tempRes.append(attr.getValue());
				break;
			default:
				tempRes.append(node.getTextContent());
				break;
			}
			if (i < nodeList.getLength() - 1) {
				tempRes.append(delimiter);
			}
		}

		// Do any substitution if required
		if (pattern != null) {
			context.getResult().append(compiledPattern.matcher(tempRes).replaceAll(substitution));
		}

	}
}
