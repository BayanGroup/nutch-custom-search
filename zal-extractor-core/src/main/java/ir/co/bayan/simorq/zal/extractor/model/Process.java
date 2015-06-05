package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext;
import org.apache.commons.lang3.Validate;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Evaluates the content.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Process extends Function {

	@XmlIDREF
	@XmlAttribute(name = "processor")
	private ProcessorDef processor;

	@SuppressWarnings("unchecked")
	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		Validate.isTrue(args != null && args.size() == 1, "Only one inner functions are expected.");

		List<Object> res = (List<Object>) args.get(0).extract(root, context);
		return processor.getProcessorInstance().process(root, context, res);
	}

	@Override
	public String toString() {
		return "Process [" + super.toString() + "]";
	}

}
