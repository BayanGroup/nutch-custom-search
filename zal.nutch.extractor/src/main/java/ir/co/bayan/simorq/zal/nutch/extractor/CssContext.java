package ir.co.bayan.simorq.zal.nutch.extractor;

import org.jsoup.nodes.Element;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class CssContext extends ExtractContext {

	private Element root;

	public CssContext(String url) {
		super(url);
	}

	/**
	 * @param root
	 *            the root to set
	 */
	public void setRoot(Element root) {
		this.root = root;
	}

	/**
	 * @return the root
	 */
	public Element getRoot() {
		return root;
	}

}
