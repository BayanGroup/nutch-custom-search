package ir.co.bayan.simorq.zal.extractor.evaluation;

import ir.co.bayan.simorq.zal.extractor.core.Content;

import java.util.List;

/**
 * Evaluates expressions from the specified content. This is usually done by parsing the provided content. Evaluator can
 * extract attributes or texts from the result of its evaluation later. Evaluator operates on a context that created
 * itself and can hold evaluation specific data.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public interface Evaluator<C extends EvaluationContext> {

	/**
	 * Evaluates the given expression.
	 */
	List<?> evaluate(Object root, C context, String expression) throws Exception;

	/**
	 * Retrieves the value of the attribute with the given from each item in the input and return their list as the
	 * result.
	 */
	List<?> getAttribute(C context, List<?> input, String name) throws Exception;

	/**
	 * Retrieves the text from each item in the input and return their list as the result.
	 */
	List<?> getText(C context, List<?> input) throws Exception;

    /**
     * Retrieves the raw data (value) from each item in the input and return their list as the result.
     */
    List<?> getRaw(C context, List<?> input) throws Exception;

	/**
	 * Creates a context for evaluation. This context will be passed in the subsequent calls.
	 */
	C createContext(Content content) throws Exception;

	/**
	 * Returns this engine name
	 */
	String getName();

}
