package ir.co.bayan.simorq.zal.extractor.model;

import static org.junit.Assert.*;

import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractorConfigurationTest {

	private ExtractorConfig extractorConfig;

	@Before
	public void setUp() throws Exception {
		InputStreamReader configReader = new InputStreamReader(getClass().getResourceAsStream(
				"/extractors-config-test.xml"));
		extractorConfig = ExtractorConfig.readConfig(configReader);
	}

	@Test
	public void test() {
		assertEquals(3, extractorConfig.getTypes().size());
		assertEquals(13, extractorConfig.getFields().size());
		assertEquals(5, extractorConfig.getDocuments().size());
		assertNotNull(extractorConfig.getDocuments().get(0).getExtractTos().get(0).getField());
		assertNotNull(extractorConfig.getFields().get(3).getType());
		Function func = extractorConfig.getDocuments().get(0).getExtractTos().get(0).getValue();
		assertTrue(func instanceof Concat);
		assertEquals(1, func.getArgs().size());
		assertTrue(func.getArgs().get(0) instanceof Replace);
		assertNotNull(extractorConfig.getDocuments().get(0).getOutlinks().get(0));
		assertTrue(extractorConfig.getDocuments().get(1).isUpdate());
	}
}
