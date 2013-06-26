package ir.co.bayan.simorq.zal.extractor.evaluation;

import java.net.URL;

import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Element;

/**
 * Context used by {@link XPathEvaluator}.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class XPathContext extends ExtractContext {

	private final NamespaceContext nsContext;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public XPathContext(Evaluator<XPathContext> engine, URL url, Element root, NamespaceContext nsContext) {
		super((Evaluator) engine, url);
		this.root = root;
		this.nsContext = nsContext;
	}

	/**
	 * @return the root
	 */
	@Override
	public Element getRoot() {
		return (Element) root;
	}

	/**
	 * @return the nsContext
	 */
	public NamespaceContext getNsContext() {
		return nsContext;
	}

}
