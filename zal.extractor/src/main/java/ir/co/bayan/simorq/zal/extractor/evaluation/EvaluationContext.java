package ir.co.bayan.simorq.zal.extractor.evaluation;

import ir.co.bayan.simorq.zal.extractor.core.Content;

/**
 * ExtractContext contains all information necessary for evaluation of extract expressions.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public abstract class EvaluationContext {

	protected Content content;
	protected Object root;
	protected final Evaluator<EvaluationContext> evaluator;

	public EvaluationContext(Evaluator<EvaluationContext> evaluator, Content content) {
		this.evaluator = evaluator;
		this.content = content;
	}

	/**
	 * @return the engine
	 */
	public Evaluator<EvaluationContext> getEvaluator() {
		return evaluator;
	}

	/**
	 * @return the content
	 */
	public Content getContent() {
		return content;
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
