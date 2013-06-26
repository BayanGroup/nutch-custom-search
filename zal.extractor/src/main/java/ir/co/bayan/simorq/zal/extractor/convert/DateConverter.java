package ir.co.bayan.simorq.zal.extractor.convert;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author ali
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 */
public class DateConverter implements Converter {

	private static final Date DEFAULT_VALUE = new Date(0);
	private static final Logger LOGGER = LoggerFactory.getLogger(DateConverter.class);

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
			LOGGER.warn("", e);
		}

		return DEFAULT_VALUE;
	}

}
