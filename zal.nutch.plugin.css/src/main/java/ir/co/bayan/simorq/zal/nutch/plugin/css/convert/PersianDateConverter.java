package ir.co.bayan.simorq.zal.nutch.plugin.css.convert;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
/**
 * 
 * @author ali
 * @author taha
 */
public class PersianDateConverter implements Converter {

	private static final Date DEFAULT_VALUE = new Date(0);
	private static final Logger logger = Logger.getLogger(PersianDateConverter.class);
	/**
	 * 
	 */
	@Override
	public Object convert(String value) {
		if(StringUtils.isEmpty(value)) {
			return DEFAULT_VALUE;
		}
		
		String[] parts = value.split("-");
		if(parts.length != 3){
			return DEFAULT_VALUE;
		}
		
		try {
			int year = PerisanConvertUtils.convertYear(parts[0]);
			int month = PerisanConvertUtils.convertMonth(parts[1]);
			int day = PerisanConvertUtils.convertDay(parts[2]);
			Calendar calendar = Calendar.getInstance();
			calendar.set(year + 600, month, day);
			return calendar.getTime();
		} catch(Exception e) {
			logger.warn("", e);
		}
		
		return DEFAULT_VALUE;
	}

}
