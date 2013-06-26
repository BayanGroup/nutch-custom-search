package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.ExtractContext;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Constant extends Function {

	@XmlAttribute
	private String value;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	@Override
	public List<?> extract(Object root, ExtractContext context) {
		return Arrays.asList(value);
	}

}
