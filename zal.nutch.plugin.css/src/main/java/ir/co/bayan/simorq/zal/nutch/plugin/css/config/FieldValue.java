package ir.co.bayan.simorq.zal.nutch.plugin.css.config;

import javax.xml.bind.annotation.XmlValue;

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

	public abstract void extract(StringBuilder res, org.jsoup.nodes.Document parsedDoc, String url);

}
