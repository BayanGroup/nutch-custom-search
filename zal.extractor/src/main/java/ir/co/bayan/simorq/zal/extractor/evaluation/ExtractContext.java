package ir.co.bayan.simorq.zal.extractor.evaluation;

import java.net.URL;

/**
 * ExtractContext contains all information necessary for evaluation of extract expressions.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public abstract class ExtractContext {

	protected URL url;
	protected Object root;
	protected final Evaluator<ExtractContext> evaluator;

	public ExtractContext(Evaluator<ExtractContext> evaluator, URL url) {
		this.evaluator = evaluator;
		this.url = url;
	}

	/**
	 * @return the engine
	 */
	public Evaluator<ExtractContext> getEvaluator() {
		return evaluator;
	}

	/**
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}

	public void setRoot(Object root) {
		this.root = root;
	}

	/**
	 * @return the root
	 */
	public Object getRoot() {
		return root;
	}

}
