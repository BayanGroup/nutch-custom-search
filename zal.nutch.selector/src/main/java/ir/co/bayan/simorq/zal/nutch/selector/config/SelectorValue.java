package ir.co.bayan.simorq.zal.nutch.selector.config;

import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public abstract class SelectorValue extends FieldValue {

	private static final String LAST_SELECT = "last";

	private static final String FIRST_SELECT = "first";

	private static final String SIZE_SELECT = "size";

	private static final String URL_SELECTOR = "url";

	@XmlAttribute
	private String selector;

	@XmlAttribute
	private final String delimiter = " ";

	private String pattern;

	private Pattern compiledPattern;

	@XmlAttribute
	private String substitution;

	@XmlAttribute
	private String select;

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern
	 *            the pattern to set
	 */
	@XmlAttribute
	public void setPattern(String pattern) {
		this.pattern = pattern;
		this.compiledPattern = Pattern.compile(pattern);
	}

	/**
	 * @return the selector
	 */
	public String getSelector() {
		return selector;
	}

	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}

	@Override
	public void extract(StringBuilder res, Element root, String url) {
		// If there is any substitution pattern, first put the extract fragment in a tempRes and then update the final
		// result after replacement
		StringBuilder tempRes;
		if (pattern == null) {
			tempRes = res;
		} else {
			tempRes = new StringBuilder();
		}

		// Fill the tempRes with the value of selector
		if (URL_SELECTOR.equals(selector)) {
			tempRes.append(url);
		} else {
			Elements elements = root.select(selector);
			if (SIZE_SELECT.equals(select)) {
				tempRes.append(elements.size());
			} else if (elements.size() > 0) {
				if (FIRST_SELECT.equals(select)) {
					extractValue(elements.first(), tempRes);
				} else if (LAST_SELECT.equals(select)) {
					extractValue(elements.last(), tempRes);
				} else {
					for (Element element : elements) {
						extractValue(element, tempRes);
						if (element != elements.last())
							tempRes.append(delimiter);
					}
				}

			}
		}

		// Do any substitution if required
		if (pattern != null) {
			res.append(compiledPattern.matcher(tempRes).replaceAll(substitution));
		}
	}

	protected abstract void extractValue(Element element, StringBuilder res);

}
