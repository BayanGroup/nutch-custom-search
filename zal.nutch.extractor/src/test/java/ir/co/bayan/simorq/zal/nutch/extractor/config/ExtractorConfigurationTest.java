package ir.co.bayan.simorq.zal.nutch.extractor.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractorConfigurationTest {

	private SelectorConfiguration config;

	@Before
	public void setUp() throws Exception {
		InputStreamReader configReader = new InputStreamReader(getClass().getResourceAsStream(
				"/extractors-config-test.xml"));
		config = SelectorConfiguration.readConfig(configReader);
	}

	@Test
	public void test() {
		assertEquals(3, config.getTypes().size());
		assertEquals(8, config.getFields().size());
		assertEquals(4, config.getDocuments().size());
		assertNotNull(config.getDocuments().get(0).getExtractTos().get(0).getField());
		assertNotNull(config.getFields().get(2).getType());
	}

}
