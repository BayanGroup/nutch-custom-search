package ir.co.bayan.simorq.zal.nutch.extractor.config;

import ir.co.bayan.simorq.zal.nutch.extractor.engine.ExtractContext;

import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.Validate;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Matches extends Function {

	private String pattern;

	private Pattern compiledPattern;

	/**
	 * @param pattern
	 *            the pattern to set
	 */
	@XmlAttribute
	public void setPattern(String pattern) {
		this.pattern = pattern;
		this.compiledPattern = Pattern.compile(pattern);
	}

	@Override
	public Object extract(ExtractContext context) throws Exception {
		Validate.isTrue(args.size() == 1, "Only one inner function is expected.");
		String res = String.valueOf(args.get(0).extract(context));

		return compiledPattern.matcher(res).matches();
	}

}
