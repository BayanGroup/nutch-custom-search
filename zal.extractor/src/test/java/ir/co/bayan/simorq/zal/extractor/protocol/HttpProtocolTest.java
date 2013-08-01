package ir.co.bayan.simorq.zal.extractor.protocol;

import static org.junit.Assert.*;
import ir.co.bayan.simorq.zal.extractor.core.Content;
import ir.co.bayan.simorq.zal.extractor.protocol.DirectHttpProtocol;
import ir.co.bayan.simorq.zal.extractor.protocol.ProtocolException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class HttpProtocolTest {

	/**
	 * Test method for
	 * {@link ir.co.bayan.simorq.zal.extractor.protocol.DirectHttpProtocol#fetch(java.net.URL, java.util.Map)}.
	 * 
	 * @throws ProtocolException
	 * @throws MalformedURLException
	 */
	@Test
	@Ignore
	public void testFetch() throws MalformedURLException, ProtocolException {
		DirectHttpProtocol protocol = new DirectHttpProtocol();
		Content content = protocol.fetch(new URL("http://www.google.com"), new HashMap<String, Object>());

		assertEquals("text/html", content.getType());
		assertEquals("ISO-8859-1", content.getEncoding());
	}

}
