package ir.co.bayan.simorq.zal.extractor.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class TruncateTest {

	/**
	 * Test method for
	 * {@link ir.co.bayan.simorq.zal.extractor.model.Truncate#extract(java.lang.Object, ir.co.bayan.simorq.zal.extractor.evaluation.ExtractContext)}
	 * .
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testExtract() throws Exception {
		Truncate truncate = new Truncate();
		Function extractor = mock(Function.class);
		when(extractor.extract(null, null)).thenReturn((List) Arrays.asList("", "a", "aa", "aaa"));
		truncate.setArgs(Arrays.asList(extractor));
		truncate.setMax(2);

		List<?> res = truncate.extract(null, null);
		assertEquals("", res.get(0));
		assertEquals("a", res.get(1));
		assertEquals("aa", res.get(2));
		assertEquals("aa", res.get(3));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testBreak() throws Exception {
		Truncate truncate = new Truncate();
		Function extractor = mock(Function.class);
		when(extractor.extract(null, null)).thenReturn((List) Arrays.asList("", "aaa", "aaaaa", "aaaaaa", "aaa aaa"));
		truncate.setArgs(Arrays.asList(extractor));
		truncate.setMax(5);
		truncate.setBreakOnWhitespaces(false);

		List<?> res = truncate.extract(null, null);
		assertEquals("", res.get(0));
		assertEquals("aaa", res.get(1));
		assertEquals("aaaaa", res.get(2));
		assertEquals("", res.get(3));
		assertEquals("aaa ", res.get(4));
	}
}
