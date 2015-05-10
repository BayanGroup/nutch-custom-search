package ir.co.bayan.simorq.zal.extractor.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class LongConverter implements Converter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LongConverter.class);
	public static final long DEFAULT_VALUE = 0L;

	@Override
	public Object convert(String value) {

		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			LOGGER.warn("", e);
		}

		return DEFAULT_VALUE;
	}

}
