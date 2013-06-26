package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.ExtractContext;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Evaluates an expression using the provided evaluator in the context.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Expr extends Function {

	@XmlAttribute
	private String value;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	@Override
	public List<?> extract(Object root, ExtractContext context) throws Exception {
		return context.getEvaluator().evaluate(root, context, value);
	}

	@Override
	public String toString() {
		return "Expr [value=" + value + "]";
	}

}
