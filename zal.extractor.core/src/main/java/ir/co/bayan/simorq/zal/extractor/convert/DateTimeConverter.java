package ir.co.bayan.simorq.zal.extractor.convert;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interprets canonical representation of date time specified at http://www.w3.org/TR/xmlschema-2/#dateTime
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class DateTimeConverter implements Converter {

	private static final Date DEFAULT_VALUE = new Date(0);
	private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeConverter.class);

	@Override
	public Object convert(String value) {
		try {
			return DateUtils.parseDate(value, "yyyy-MM-dd'T'HH:mm:ss");
		} catch (ParseException e) {
			LOGGER.warn("", e);
			return DEFAULT_VALUE;
		}
	}

}
