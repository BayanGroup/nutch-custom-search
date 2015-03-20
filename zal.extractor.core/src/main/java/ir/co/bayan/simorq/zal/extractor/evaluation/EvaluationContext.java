package ir.co.bayan.simorq.zal.extractor.evaluation;

import ir.co.bayan.simorq.zal.extractor.core.Content;
import ir.co.bayan.simorq.zal.extractor.core.ExtractedDoc;

/**
 * EvaluationContext contains all information necessary for evaluation of extract expressions by {@link Evaluator}s.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * @see Evaluator
 */
public abstract class EvaluationContext {

	protected Content content;
	protected Object mainRoot;
	protected final Evaluator<EvaluationContext> evaluator;
	protected ExtractedDoc currentDoc;

	public EvaluationContext(Evaluator<EvaluationContext> evaluator, Content content, Object mainRoot) {
		this.evaluator = evaluator;
		this.content = content;
		this.mainRoot = mainRoot;
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

	public void setMainRoot(Object root) {
		this.mainRoot = root;
	}

	/**
	 * @return the root
	 */
	public Object getMainRoot() {
		return mainRoot;
	}

	/**
	 * @return the currentDoc
	 */
	public ExtractedDoc getCurrentDoc() {
		return currentDoc;
	}

	/**
	 * @param currentDoc
	 *            the currentDoc to set
	 */
	public void setCurrentDoc(ExtractedDoc currentDoc) {
		this.currentDoc = currentDoc;
	}

}
