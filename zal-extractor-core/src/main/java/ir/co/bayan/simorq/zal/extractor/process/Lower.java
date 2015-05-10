package ir.co.bayan.simorq.zal.extractor.process;

/**
 * Converts each input string to lower case.
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Lower implements Processor {

	@Override
	public Object process(Object input) {
		return ((String) input).toLowerCase();
	}

}
