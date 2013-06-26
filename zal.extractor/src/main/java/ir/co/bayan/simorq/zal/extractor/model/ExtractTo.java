package ir.co.bayan.simorq.zal.extractor.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractTo extends FunctionHolder {

	@XmlIDREF
	@XmlAttribute
	private Field field;

	public Field getField() {
		return field;
	}

	@Override
	public String toString() {
		return "ExtractTo [field=" + field + ", value=" + getValue() + "]";
	}

}
