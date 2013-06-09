package ir.co.bayan.simorq.zal.nutch.selector.config;

import javax.xml.bind.annotation.XmlValue;

import org.jsoup.nodes.Element;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */

public abstract class FieldValue {

	@XmlValue
	protected String value;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	public abstract void extract(StringBuilder res, Element root, String url);

}
