package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * These extractors change the root of the extraction process.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public abstract class Rooted implements Extractor {

	@XmlAttribute
	private String root;

	/**
	 * @return the root
	 */
	public String getRoot() {
		return root;
	}

	protected List<?> getRoots(Object rootObj, EvaluationContext context) throws Exception {
		if (root == null)
			return Arrays.asList(rootObj);
		return context.getEvaluator().evaluate(rootObj, context, root);
	}

}
