package ir.co.bayan.simorq.zal.nutch.extractor.config;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Partition {

	@XmlElement
	private Expr expr;

	/**
	 * @return the expr
	 */
	public Expr getExpr() {
		return expr;
	}

}
