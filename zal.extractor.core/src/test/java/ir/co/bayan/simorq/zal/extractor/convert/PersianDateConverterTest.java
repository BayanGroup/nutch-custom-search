package ir.co.bayan.simorq.zal.extractor.convert;

import static org.junit.Assert.*;

import ir.co.bayan.simorq.zal.extractor.convert.PersianDateConverter;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class PersianDateConverterTest {

	private static PersianDateConverter converter;

	@BeforeClass
	public static void init() {
		converter = new PersianDateConverter();
	}

	@Test
	public void testConvert() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(1390 + 600, 3, 9);
		assertTrue(DateUtils.isSameDay(calendar.getTime(), (Date) converter.convert("90-3-09")));
	}

}
