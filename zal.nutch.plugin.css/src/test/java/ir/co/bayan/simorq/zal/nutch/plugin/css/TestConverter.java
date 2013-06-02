package ir.co.bayan.simorq.zal.nutch.plugin.css;

import ir.co.bayan.simorq.zal.nutch.plugin.css.convert.Converter;

public class TestConverter implements Converter {

	@Override
	public Object convert(String value) {
		return "Hello " + value;
	}

}
