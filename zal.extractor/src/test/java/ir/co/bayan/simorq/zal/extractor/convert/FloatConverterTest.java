package ir.co.bayan.simorq.zal.extractor.convert;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class FloatConverterTest {

	private static FloatConverter converter;

	@BeforeClass
	public static void init() {
		converter = new FloatConverter();
	}

	@Test
	public void testConvertTypical() {
		assertEquals(12345f, converter.convert("12345"));
		assertEquals(12.345f, converter.convert("12.345"));
		assertEquals(0.12345f, converter.convert("0.12345"));
		assertEquals(0.12345f, converter.convert(".12345"));
	}

	@Test
	public void testConvertEmpty() {
		assertEquals(FloatConverter.DEFAULT_VALUE, converter.convert(""));
		assertEquals(FloatConverter.DEFAULT_VALUE, converter.convert(null));
	}

	@Test
	public void testConvertWithError() {
		assertEquals(FloatConverter.DEFAULT_VALUE, converter.convert("1.2."));
		assertEquals(FloatConverter.DEFAULT_VALUE, converter.convert("12y"));
	}

}
