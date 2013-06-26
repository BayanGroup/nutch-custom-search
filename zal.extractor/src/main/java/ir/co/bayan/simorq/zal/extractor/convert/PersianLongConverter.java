package ir.co.bayan.simorq.zal.extractor.convert;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class PersianLongConverter implements Converter {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersianLongConverter.class);
	private static final long DEFAULT_VALUE = 0L;

	@Override
	public Object convert(String value) {
		if (StringUtils.isEmpty(value)) {
			return DEFAULT_VALUE;
		}

		String convertedValue = PerisanConvertUtils.covertNumbers(value);
		try {
			return Long.parseLong(convertedValue);
		} catch (Exception e) {
			LOGGER.warn("", e);
		}

		return DEFAULT_VALUE;
	}

}
