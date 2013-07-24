package ir.co.bayan.simorq.zal.extractor.evaluation;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class EvaluatorFactory {

	private final Map<String, Evaluator<? extends EvaluationContext>> evaluators;

	private static EvaluatorFactory instance;

	/**
	 * @return the instance
	 */
	public static EvaluatorFactory getInstance() {
		if (instance == null) {
			instance = new EvaluatorFactory();
		}
		return instance;
	}

	public EvaluatorFactory() {
		evaluators = new HashMap<>();
		addEvaluator(new CssEvaluator());
		addEvaluator(new XPathEvaluator());
		addEvaluator(new TextEvaluator());
	}

	public Evaluator<? extends EvaluationContext> getEvaluator(String name) {
		Evaluator<? extends EvaluationContext> res = evaluators.get(name);
		Validate.notNull(res, "No evaluator found with name " + name);
		return res;
	}

	public void addEvaluator(Evaluator<? extends EvaluationContext> evaluator) {
		Validate.notNull(evaluator);

		evaluators.put(evaluator.getName(), evaluator);
	}

}
