package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Maps input strings to another strings
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Map extends Function {

	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		return null;
	}

}
