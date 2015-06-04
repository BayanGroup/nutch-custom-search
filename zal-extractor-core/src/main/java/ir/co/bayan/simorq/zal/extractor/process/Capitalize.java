package ir.co.bayan.simorq.zal.extractor.process;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Capitalizes each input string.
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Capitalize implements Processor {

	@Override
	public List<Object> process(List<Object> input) {
		for(int i = 0; i < input.size(); i++)
			input.set(i, StringUtils.capitalize((String) input.get(i)));
		return input;
	}

}
