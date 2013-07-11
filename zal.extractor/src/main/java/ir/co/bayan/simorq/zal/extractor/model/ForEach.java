package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Iterates through its children for each possible value of root.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement(name = "for-each")
public class ForEach extends Function {

	@XmlAttribute(required = true)
	private String root;

	protected List<?> getRoots(Object rootObj, EvaluationContext context) throws Exception {
		if (root == null)
			return Arrays.asList(rootObj);
		return context.getEvaluator().evaluate(rootObj, context, root);
	}

	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		List<Object> res = new ArrayList<>();
		for (Object newRoot : getRoots(root, context)) {
			res.addAll(args.get(0).extract(newRoot, context));
		}
		return res;
	}

	@Override
	public String toString() {
		return "ForEach [root=" + root + ", args=" + args + "]";
	}

}
