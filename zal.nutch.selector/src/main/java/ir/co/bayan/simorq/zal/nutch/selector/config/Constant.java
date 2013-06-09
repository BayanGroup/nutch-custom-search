package ir.co.bayan.simorq.zal.nutch.selector.config;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsoup.nodes.Element;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Constant extends FieldValue {

	@Override
	public void extract(StringBuilder res, Element root, String url) {
		res.append(value);
	}

}
