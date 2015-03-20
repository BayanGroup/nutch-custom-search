package ir.co.bayan.simorq.zal.extractor.convert;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
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

		try {
			return DateUtils.parseDate(value, "dd/MM/yyyy");
		} catch (Exception e) {
			LOGGER.warn("", e);
		}

		return DEFAULT_VALUE;
	}

}
