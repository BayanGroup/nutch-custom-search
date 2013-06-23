package ir.co.bayan.simorq.zal.nutch.extractor.config;

import ir.co.bayan.simorq.zal.nutch.extractor.engine.ExtractContext;

import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class FunctionHolder {

	@XmlElementRef(required = true)
	private Function value;

	/**
	 * @return the value
	 */
	public Function getValue() {
		return value;
	}

	@SuppressWarnings("unchecked")
	public List<String> extract(ExtractContext context) throws Exception {
		return (List<String>) value.extract(context);
	}

}
