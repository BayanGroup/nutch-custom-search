package ir.co.bayan.simorq.zal.nutch.plugin.css.config;

import javax.xml.bind.annotation.XmlRootElement;

import org.jsoup.nodes.Element;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Content extends SelectorValue {

	@Override
	protected void extractValue(Element element, StringBuilder res) {
		res.append(element.text());
	}

}
