package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.core.Content;
import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext;
import ir.co.bayan.simorq.zal.extractor.evaluation.Evaluator;
import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluatorFactory;
import ir.co.bayan.simorq.zal.extractor.protocol.Protocol;
import ir.co.bayan.simorq.zal.extractor.protocol.ProtocolFactory;
import org.junit.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class FetchTest {

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

}
