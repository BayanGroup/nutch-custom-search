package ir.co.bayan.simorq.zal.nutch.selector.convert;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class LongConverter implements Converter {
	
	private static final Logger logger = Logger.getLogger(LongConverter.class);
	
	public static final long DEFAULT_VALUE = 0L;
	
	@Override
	public Object convert(String value) {
		if (StringUtils.isEmpty(value)) {
			return DEFAULT_VALUE;
		}

		try {
			return Long.parseLong(value);
		} catch(Exception e) {
			logger.warn("", e);
		}
		
		return DEFAULT_VALUE;
	}

	

}
