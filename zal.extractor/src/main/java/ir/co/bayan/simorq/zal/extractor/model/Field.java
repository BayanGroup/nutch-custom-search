package ir.co.bayan.simorq.zal.extractor.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Field {

	@XmlAttribute
	@XmlID
	private String name;

	@XmlIDREF
	@XmlAttribute(name = "type")
	private TypeDef type;

	@XmlAttribute
	private boolean multi = false;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public TypeDef getType() {
		return type;
	}

	/**
	 * @return the multi
	 */
	public boolean isMulti() {
		return multi;
	}

	@Override
	public String toString() {
		return "Field [name=" + name + "]";
	}

}
