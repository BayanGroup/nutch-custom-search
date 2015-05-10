package ir.co.bayan.simorq.zal.extractor.convert;

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
