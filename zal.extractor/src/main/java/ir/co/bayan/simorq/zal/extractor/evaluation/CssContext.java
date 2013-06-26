package ir.co.bayan.simorq.zal.extractor.evaluation;

import java.net.URL;

import org.jsoup.nodes.Element;

/**
 * Context used by {@link CssEvaluator}.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class CssContext extends ExtractContext {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CssContext(Evaluator<CssContext> engine, URL url, Element root) {
		super((Evaluator) engine, url);
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
