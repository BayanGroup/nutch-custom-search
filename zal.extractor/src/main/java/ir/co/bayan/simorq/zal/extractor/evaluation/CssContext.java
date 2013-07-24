package ir.co.bayan.simorq.zal.extractor.evaluation;

import ir.co.bayan.simorq.zal.extractor.core.Content;

import org.jsoup.nodes.Element;

/**
 * Context used by {@link CssEvaluator}.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class CssContext extends EvaluationContext {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CssContext(Evaluator<CssContext> engine, Content content, Element root) {
		super((Evaluator) engine, content, root);
	}

	/**
	 * @return the root
	 */
	@Override
	public Element getRoot() {
		return (Element) root;
	}

}
