package ir.co.bayan.simorq.zal.nutch.extractor.engine;

import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Element;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class XPathContext extends ExtractContext {

	private final NamespaceContext nsContext;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public XPathContext(ExtractEngine<XPathContext> engine, String url, Element root, NamespaceContext nsContext) {
		super((ExtractEngine) engine, url);
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
