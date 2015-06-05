package ir.co.bayan.simorq.zal.extractor.process;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Converts each input string to upper case.
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Upper extends StringProcessor {

	@Override
	protected String process(String item) {
		return StringUtils.upperCase(item);
	}
}
