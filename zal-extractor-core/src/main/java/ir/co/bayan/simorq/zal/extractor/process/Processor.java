package ir.co.bayan.simorq.zal.extractor.process;

import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext;

import java.util.List;

/**
 * Processes a given input object an produces a result. Processors can be used in process function to post-process
 * result of other functions.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public interface Processor {

	/**
	 * Processes the given input list and produces a list of results.
	 * @param root The current root object under which evaluation is performed.
	 * @param context The evaluation content.
	 * @param input The input list produces by inner functions.
	 * @return A list of results to be processed by next functions in the chain. Note that
	 * the result must not be null.
	 */
	List<Object> process(Object root, EvaluationContext context, List<Object> input);

}
