package ir.co.bayan.simorq.zal.extractor.model;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import ir.co.bayan.simorq.zal.extractor.core.Content;
import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext;
import ir.co.bayan.simorq.zal.extractor.evaluation.Evaluator;
import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluatorFactory;
import ir.co.bayan.simorq.zal.extractor.model.Function;
import ir.co.bayan.simorq.zal.extractor.protocol.FileProtocol;
import ir.co.bayan.simorq.zal.extractor.protocol.Protocol;
import ir.co.bayan.simorq.zal.extractor.protocol.ProtocolException;
import ir.co.bayan.simorq.zal.extractor.protocol.ProtocolFactory;
import ir.co.bayan.simorq.zal.extractor.protocol.ProtocolException.ProtocolErrorCode;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class FetchTest {

	/**
	 * Test method for
	 * {@link ir.co.bayan.simorq.zal.nutch.watcher.model.Fetch#extract(java.lang.Object, ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext)}
	 * .
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testExtract() throws Exception {
		Fetch fetch = new Fetch();

		Content content = mock(Content.class);

		fetch.setEngine("txt");
		assertEquals(EvaluatorFactory.getInstance().getEvaluator("txt"), fetch.getEvaluator());
		Evaluator evaluator = mock(Evaluator.class);
		EvaluationContext context = mock(EvaluationContext.class);
		when(evaluator.createContext(content)).thenReturn(context);
		fetch.setEvaluator(evaluator);

		fetch.setUrl(getClass().getResource("/sample.txt").toString());
		assertEquals(ProtocolFactory.getInstance().getProtocol(fetch.getUrl()), fetch.getProtocol());
		Protocol protocol = mock(Protocol.class);
		when(protocol.fetch(any(URL.class), any(java.util.Map.class))).thenReturn(content);
		fetch.setProtocol(protocol);

		Function dummy = mock(Function.class);
		Object res0 = new Object();
		when(dummy.extract(any(), any(EvaluationContext.class))).thenReturn((List) Arrays.asList(res0));
		fetch.setArgs(Arrays.asList(dummy));

		List<?> extract = fetch.extract(null, null);
		verify(protocol).fetch(any(URL.class), any(java.util.Map.class));
		verify(evaluator).createContext(content);
		assertEquals(res0, extract.get(0));

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testCheckModified() throws Exception {
		Fetch fetch = new Fetch();
		fetch.setCheckModified(true);

		fetch.setEngine("txt");
		fetch.setUrl(getClass().getResource("/sample.txt").toString());
		fetch.setParameters(Arrays.asList(new FetchParameter(FileProtocol.PARAM_ENCODING, "UTF-8")));

		Function dummy = mock(Function.class);
		Object res0 = new Object();
		when(dummy.extract(any(), any(EvaluationContext.class))).thenReturn((List) Arrays.asList(res0));
		fetch.setArgs(Arrays.asList(dummy));

		fetch.extract(null, null);
		// since check modified is true it should throws an exception now
		boolean notChanged = false;
		try {
			fetch.extract(null, null);
		} catch (ProtocolException e) {
			notChanged = e.getCode() == ProtocolErrorCode.NOT_CHANGED;
		}
		assertTrue(notChanged);
	}
}
