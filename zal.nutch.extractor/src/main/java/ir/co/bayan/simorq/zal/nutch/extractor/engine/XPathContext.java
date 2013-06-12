package ir.co.bayan.simorq.zal.nutch.extractor.engine;

import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Element;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class XPathContext extends ExtractContext {

	private final Element root;
	private final NamespaceContext nsContext;

	public XPathContext(String url, Element root, NamespaceContext nsContext) {
		super(url);
		this.root = root;
		this.nsContext = nsContext;
	}

	/**
	 * @return the root
	 */
	public Element getRoot() {
		return root;
	}

	/**
	 * @return the nsContext
	 */
	public NamespaceContext getNsContext() {
		return nsContext;
	}

}
