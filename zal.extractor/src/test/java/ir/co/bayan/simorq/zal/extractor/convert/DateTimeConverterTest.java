package ir.co.bayan.simorq.zal.extractor.convert;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class DateTimeConverterTest {

	private static DateTimeConverter converter;

	@BeforeClass
	public static void init() {
		converter = new DateTimeConverter();
	}

	/**
	 * Test method for {@link ir.co.bayan.simorq.zal.extractor.convert.DateTimeConverter#convert(java.lang.String)}.
	 */
	@Test
	public void testConvert() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2013, 3 - 1, 9, 20, 10, 5);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime((Date) converter.convert("2013-03-09T20:10:5"));
		calendar.set(Calendar.MILLISECOND, calendar2.get(Calendar.MILLISECOND));
		assertEquals(calendar2.getTime(), calendar.getTime());

	}
}
