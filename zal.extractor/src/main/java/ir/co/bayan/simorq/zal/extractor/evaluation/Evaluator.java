package ir.co.bayan.simorq.zal.extractor.evaluation;

import java.util.List;

/**
 * Evaluates a given expression in a way that it can extract its attributes or texts later. Evaluator operates on a
 * context that created itself and can hold evaluation specific data.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public interface Evaluator<C extends ExtractContext> {

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
	 * Creates a context for evaluation. This context will be passed in the subsequent calls.
	 */
	C createContext(String url, byte[] content, String encoding, String contentType) throws Exception;

}
