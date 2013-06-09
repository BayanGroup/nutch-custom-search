package ir.co.bayan.simorq.zal.nutch.selector.convert;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class PersianLongConverter implements Converter {
	
	private static final Logger logger = Logger.getLogger(PersianLongConverter.class);
	
	private static final long DEFAULT_VALUE = 0L;
	
	@Override
	public Object convert(String value) {
		if (StringUtils.isEmpty(value)) {
			return DEFAULT_VALUE;
		}

		String convertedValue = PerisanConvertUtils.covertNumbers(value);
		try {
			return Long.parseLong(convertedValue);
		} catch(Exception e) {
			logger.warn("", e);
		}
		
		return DEFAULT_VALUE;
	}

	

}
