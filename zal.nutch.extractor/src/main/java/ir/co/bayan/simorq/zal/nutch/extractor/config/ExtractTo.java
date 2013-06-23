package ir.co.bayan.simorq.zal.nutch.extractor.config;

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

}
