package ir.co.bayan.simorq.zal.nutch.selector.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractTo {

	@XmlIDREF
	@XmlAttribute
	private Field field;

	@XmlElementRef
	private List<FieldValue> values;

	@XmlAttribute
	private String delimiter = " ";

	public Field getField() {
		return field;
	}

	/**
	 * @return the values
	 */
	public List<FieldValue> getValues() {
		return values;
	}

	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}

}
