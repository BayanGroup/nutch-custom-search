package ir.co.bayan.simorq.zal.nutch.selector.convert;

import static org.junit.Assert.*;
import ir.co.bayan.simorq.zal.nutch.selector.convert.LongConverter;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class LongConverterTest {

	private static LongConverter converter;

	@BeforeClass
	public static void init() {
		converter = new LongConverter();
	}

	@Test
	public void testConvertTypical() {
		assertEquals(12345L, converter.convert("12345"));
	}

	@Test
	public void testConvertEmpty() {
		assertEquals(LongConverter.DEFAULT_VALUE, converter.convert(""));
		assertEquals(LongConverter.DEFAULT_VALUE, converter.convert(null));
	}

	@Test
	public void testConvertWithError() {
		assertEquals(LongConverter.DEFAULT_VALUE, converter.convert("1.2"));
		assertEquals(LongConverter.DEFAULT_VALUE, converter.convert("12y"));
	}

}
