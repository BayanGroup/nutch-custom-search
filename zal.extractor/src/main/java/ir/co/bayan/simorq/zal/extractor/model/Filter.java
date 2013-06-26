package ir.co.bayan.simorq.zal.extractor.model;

import javax.xml.bind.annotation.XmlElementRef;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Filter {

	@XmlElementRef
	private Function value;

	/**
	 * @return the value
	 */
	public Function getValue() {
		return value;
	}

}
