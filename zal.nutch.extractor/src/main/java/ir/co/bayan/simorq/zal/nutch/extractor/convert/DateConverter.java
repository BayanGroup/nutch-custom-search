package ir.co.bayan.simorq.zal.nutch.extractor.convert;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @author ali
 * @author taha
 */
public class DateConverter implements Converter {

	private static final Date DEFAULT_VALUE = new Date(0);
	private static final Logger logger = Logger.getLogger(DateConverter.class);

	/**
	 * 
	 */
	@Override
	public Object convert(String value) {
		if (StringUtils.isEmpty(value)) {
			return DEFAULT_VALUE;
		}

		String[] parts = value.split("-");
		if (parts.length != 3) {
			return DEFAULT_VALUE;
		}

		try {
			int year = Integer.parseInt(parts[0]);
			int month = Integer.parseInt(parts[1]);
			int day = Integer.parseInt(parts[2]);
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, day);
			return calendar.getTime();
		} catch (Exception e) {
			logger.warn("", e);
		}

		return DEFAULT_VALUE;
	}

}
