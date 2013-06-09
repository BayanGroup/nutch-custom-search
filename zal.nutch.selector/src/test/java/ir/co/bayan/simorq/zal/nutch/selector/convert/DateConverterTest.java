package ir.co.bayan.simorq.zal.nutch.selector.convert;

import static org.junit.Assert.assertTrue;

import ir.co.bayan.simorq.zal.nutch.selector.convert.DateConverter;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class DateConverterTest {

	private static DateConverter converter;

	@BeforeClass
	public static void init() {
		converter = new DateConverter();
	}

	@Test
	public void test() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2013, 3, 9);
		assertTrue(DateUtils.isSameDay(calendar.getTime(), (Date) converter.convert("2013-03-09")));
	}

}
