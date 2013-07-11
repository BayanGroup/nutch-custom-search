package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext;

import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.Validate;

/**
 * Replaces its input using the provided regex pattern by the provided substitution.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Replace extends Function {

	private String pattern;

	private Pattern compiledPattern;

	@XmlAttribute
	private String substitution;

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
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		Validate.isTrue(args != null && args.size() == 1, "Only one inner function is expected.");
		List<String> list = (List<String>) args.get(0).extract(root, context);
		for (int i = 0; i < list.size(); i++) {
			String item = list.get(i);
			if (item != null)
				item = compiledPattern.matcher(item).replaceAll(substitution);
			list.set(i, item);
		}
		return list;
	}

	@Override
	public String toString() {
		return "Replace [pattern=" + pattern + ", substitution=" + substitution + "]";
	}

}
