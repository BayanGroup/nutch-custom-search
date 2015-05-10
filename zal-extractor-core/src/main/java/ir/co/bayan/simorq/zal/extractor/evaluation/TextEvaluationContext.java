package ir.co.bayan.simorq.zal.extractor.evaluation;

import ir.co.bayan.simorq.zal.extractor.core.Content;

import java.io.BufferedReader;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class TextEvaluationContext extends EvaluationContext {

	private BufferedReader reader;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TextEvaluationContext(Evaluator<TextEvaluationContext> evaluator, Content content, BufferedReader reader) {
		super((Evaluator) evaluator, content, null);
		this.reader = reader;
	}

	/**
	 * @return the reader
	 */
	public BufferedReader getReader() {
		return reader;
	}

}
