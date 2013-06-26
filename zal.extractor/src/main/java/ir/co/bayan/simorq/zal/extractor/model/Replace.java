package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.ExtractContext;

import java.util.ArrayList;
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

	@Override
	public List<?> extract(Object root, ExtractContext context) throws Exception {
		Validate.isTrue(args != null && args.size() == 1, "Only one inner function is expected.");
		List<?> list = args.get(0).extract(root, context);
		List<String> changed = new ArrayList<>(list.size());
		for (Object item : list) {
			changed.add(compiledPattern.matcher(String.valueOf(item)).replaceAll(substitution));
		}
		return changed;
	}

	@Override
	public String toString() {
		return "Replace [pattern=" + pattern + ", substitution=" + substitution + "]";
	}

}
