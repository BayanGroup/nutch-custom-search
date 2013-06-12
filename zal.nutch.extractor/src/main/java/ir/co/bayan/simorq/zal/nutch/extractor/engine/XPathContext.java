package ir.co.bayan.simorq.zal.nutch.extractor.engine;

import org.w3c.dom.Element;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class XPathContext extends ExtractContext {

	private final Element root;

	public XPathContext(String url, Element root) {
		super(url);
		this.root = root;
	}

	/**
	 * @return the root
	 */
	public Element getRoot() {
		return root;
	}

}
