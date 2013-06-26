package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.ExtractContext;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.Validate;

/**
 * Returns the text content of its input.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Text extends Function {

	@Override
	public List<?> extract(Object root, ExtractContext context) throws Exception {
		Validate.isTrue(args != null && args.size() == 1, "Only one arg should be specified");
		List<?> res = args.get(0).extract(root, context);
		return context.getEvaluator().getText(context, res);
	}

	@Override
	public String toString() {
		return "Text [" + super.toString() + "]";
	}
}
