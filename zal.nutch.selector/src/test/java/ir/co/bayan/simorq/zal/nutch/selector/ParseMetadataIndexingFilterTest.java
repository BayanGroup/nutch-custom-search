package ir.co.bayan.simorq.zal.nutch.selector;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.indexer.IndexingException;
import org.apache.nutch.indexer.NutchDocument;
import org.apache.nutch.metadata.Metadata;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ParseMetadataIndexingFilterTest {

	private SelectorIndexingFilter filter;

	@Before
	public void setUp() throws Exception {
		filter = new SelectorIndexingFilter();
		Configuration configuration = mock(Configuration.class);
		InputStream confStream = getClass().getResourceAsStream("/selectors-index-test.xml");
		when(configuration.getConfResourceAsInputStream(anyString())).thenReturn(confStream);
		filter.setConf(configuration);
	}

	@Test
	public void testFilter() throws IndexingException {
		NutchDocument doc = mock(NutchDocument.class);
		Metadata metadata = mock(Metadata.class);
		when(metadata.getValues("f1")).thenReturn(new String[] { "t1" });

		filter.addFieldsToDoc(doc, metadata);
		verify(doc).add("f1", "Hello t1");
	}
}
