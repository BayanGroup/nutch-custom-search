package ir.co.bayan.simorq.zal.extractor.nutch;

import ir.co.bayan.simorq.zal.extractor.model.ExtractorConfig;
import ir.co.bayan.simorq.zal.extractor.model.Field;
import ir.co.bayan.simorq.zal.extractor.model.TypeDef;

import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.Inlinks;
import org.apache.nutch.indexer.IndexingException;
import org.apache.nutch.indexer.IndexingFilter;
import org.apache.nutch.indexer.NutchDocument;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.Parse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractorIndexingFilter implements IndexingFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtractorIndexingFilter.class);

	public static final String MATCHED_DOC = "matched-doc";
	public static final String UPDATE_DOC = "update-doc";

	private Configuration configuration;
	private ExtractorConfig extractorConfig;

	@Override
	public Configuration getConf() {
		return configuration;
	}

	@Override
	public void setConf(Configuration configuration) {
		this.configuration = configuration;
		try {
			initConfig(configuration);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	private void initConfig(Configuration configuration) throws Exception {
		extractorConfig = ExtractorConfig.readConfig(configuration);
	}

	@Override
	public NutchDocument filter(NutchDocument doc, Parse parse, Text text, CrawlDatum crawlDatum, Inlinks inlinks)
			throws IndexingException {
		Metadata metadata = parse.getData().getParseMeta();
		if ("true".equals(metadata.get(MATCHED_DOC))) {
			addFieldsToDoc(doc, metadata);
			if ("true".equals(metadata.get(UPDATE_DOC))) {
				// Passing a map will activate partial update process of solr
				doc.add("updateIndicator", new HashMap<>());
			}
			return doc;
		} else if (extractorConfig.isOmitNonMatching()) {
			return null;
		} else {
			return doc;
		}
	}

	void addFieldsToDoc(NutchDocument doc, Metadata metadata) {
		for (Field field : extractorConfig.getFields()) {
			String name = field.getName();
			for (String value : metadata.getValues(name)) {
				Object finalValue = value;
				TypeDef type = field.getType();
				if (type != null) {
					if (type.getConverterInstance() != null) {
						finalValue = type.getConverterInstance().convert(value);
					}
				}
				doc.add(name, finalValue);
			}
		}
	}

}
