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
public class PersianDateConverter implements Converter {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersianDateConverter.class);
	private static final Date DEFAULT_VALUE = new Date(0);

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
			int year = PerisanConvertUtils.convertYear(parts[0]);
			int month = PerisanConvertUtils.convertMonth(parts[1]);
			int day = PerisanConvertUtils.convertDay(parts[2]);
			Calendar calendar = Calendar.getInstance();
			calendar.set(year + 600, month, day);
			return calendar.getTime();
		} catch (Exception e) {
			LOGGER.warn("", e);
		}

		return DEFAULT_VALUE;
	}

}
