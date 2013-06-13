package ir.co.bayan.simorq.zal.nutch.extractor.engine;

import org.jsoup.nodes.Element;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class CssContext extends ExtractContext {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CssContext(ExtractEngine<CssContext> engine, String url, Element root) {
		super((ExtractEngine) engine, url);
		this.root = root;
	}

	/**
	 * @return the root
	 */
	@Override
	public Element getRoot() {
		return (Element) root;
	}

}
