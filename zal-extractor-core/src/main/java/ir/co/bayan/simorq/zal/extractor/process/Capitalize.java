package ir.co.bayan.simorq.zal.extractor.process;

import org.apache.commons.lang3.StringUtils;

/**
 * Capitalizes each input string.
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Capitalize implements Processor {

	@Override
	public Object process(Object input) {
		return StringUtils.capitalize((String) input);
	}

}
