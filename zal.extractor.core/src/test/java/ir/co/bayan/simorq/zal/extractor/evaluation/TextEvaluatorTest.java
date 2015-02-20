package ir.co.bayan.simorq.zal.extractor.evaluation;

import static org.junit.Assert.*;
import ir.co.bayan.simorq.zal.extractor.core.Content;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.Test;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class TextEvaluatorTest {

	/**
	 * Test method for
	 * {@link ir.co.bayan.simorq.zal.extractor.evaluation.TextEvaluator#createContext(ir.co.bayan.simorq.zal.extractor.core.Content)}
	 * .
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@Test
	public void testLine() throws Exception {
		TextEvaluator evaluator = new TextEvaluator();

		TextEvaluationContext context = evaluator.createContext(new Content(new URL("file://sample.txt"), getClass()
				.getResourceAsStream("/sample.txt"), "UTF-8", null));

		List<?> res = evaluator.evaluate(null, context, "line");
		assertEquals(2, res.size());
		assertEquals("l1", res.get(0));
	}

}
