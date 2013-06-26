package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.ExtractContext;

import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;

/**
 * Function holder extracts its data by delegating the extraction process to a function.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class FunctionHolder implements Extractor {

	@XmlElementRef(required = true)
	private Function value;

	/**
	 * @return the value
	 */
	public Function getValue() {
		return value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> extract(Object root, ExtractContext context) throws Exception {
		return (List<String>) value.extract(root, context);
	}

}
