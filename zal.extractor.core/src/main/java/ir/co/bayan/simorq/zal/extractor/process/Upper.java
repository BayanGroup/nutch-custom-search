package ir.co.bayan.simorq.zal.extractor.process;

/**
 * Converts each input string to upper case.
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Upper implements Processor {

	@Override
	public Object process(Object input) {
		return ((String) input).toUpperCase();
	}

}
