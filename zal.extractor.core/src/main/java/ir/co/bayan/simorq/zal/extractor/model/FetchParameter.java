package ir.co.bayan.simorq.zal.extractor.model;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class FetchParameter {

	@XmlAttribute
	private String name;

	@XmlAttribute
	private String value;

	public FetchParameter() {
	}

	public FetchParameter(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "FetchParameter [name=" + name + ", value=" + value + "]";
	}

}
