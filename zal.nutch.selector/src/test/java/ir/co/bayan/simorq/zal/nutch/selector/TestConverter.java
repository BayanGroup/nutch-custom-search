package ir.co.bayan.simorq.zal.nutch.selector;

import ir.co.bayan.simorq.zal.nutch.selector.convert.Converter;

/**
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class TestConverter implements Converter {

	@Override
	public Object convert(String value) {
		return "Hello " + value;
	}

}
