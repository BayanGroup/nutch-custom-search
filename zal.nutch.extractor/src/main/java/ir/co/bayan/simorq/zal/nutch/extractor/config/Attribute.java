package ir.co.bayan.simorq.zal.nutch.extractor.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.jsoup.nodes.Element;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Attribute extends SelectorValue {

	@XmlAttribute
	private String name;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	protected void extractValue(Element element, StringBuilder res) {
		res.append(element.attr(name));
	}

}
