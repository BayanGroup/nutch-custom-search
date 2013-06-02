package ir.co.bayan.simorq.zal.nutch.plugin.css.config;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsoup.nodes.Document;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Constant extends FieldValue {

	@Override
	public void extract(StringBuilder res, Document parsedDoc, String url) {
		res.append(value);
	}

}
