package ir.co.bayan.simorq.zal.nutch.extractor.config;

import ir.co.bayan.simorq.zal.nutch.extractor.ExtractContext;

import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement(name = "xpath")
public class XPath extends FieldValue {

	@XmlAttribute
	private String expression;

	@XmlAttribute
	private final String delimiter = " ";

	private String pattern;

	private Pattern compiledPattern;

	@XmlAttribute
	private String substitution;

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
	public String getExpression() {
		return expression;
	}

	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}

	@Override
	public void extract(ExtractContext context) {
	}

}
