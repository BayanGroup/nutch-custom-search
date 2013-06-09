package ir.co.bayan.simorq.zal.nutch.selector;

import ir.co.bayan.simorq.zal.nutch.selector.config.Field;
import ir.co.bayan.simorq.zal.nutch.selector.config.SelectorConfiguration;
import ir.co.bayan.simorq.zal.nutch.selector.config.TypeDef;

import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.log4j.Logger;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.Inlinks;
import org.apache.nutch.indexer.IndexingException;
import org.apache.nutch.indexer.IndexingFilter;
import org.apache.nutch.indexer.NutchDocument;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.Parse;

/**
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class SelectorIndexingFilter implements IndexingFilter {

	private static final Logger logger = Logger.getLogger(SelectorIndexingFilter.class);

	private Configuration configuration;
	private SelectorConfiguration config;

	@Override
	public Configuration getConf() {
		return configuration;
	}

	@Override
	public void setConf(Configuration configuration) {
		this.configuration = configuration;
		try {
			initConfig(configuration);
		} catch (UnsupportedEncodingException | JAXBException e) {
			logger.error("", e);
		}
	}

	private void initConfig(Configuration configuration) throws UnsupportedEncodingException, JAXBException {
		config = SelectorConfiguration.readConfig(configuration);
	}

	@Override
	public NutchDocument filter(NutchDocument doc, Parse parse, Text text, CrawlDatum crawlDatum, Inlinks inlinks)
			throws IndexingException {
		Metadata metadata = parse.getData().getParseMeta();
		if ("true".equals(metadata.get(SelectorParseFilter.MATCHED_DOC))) {
			addFieldsToDoc(doc, metadata);
			return doc;
		} else if (config.isOmitNonMatching()) {
			return null;
		} else {
			return doc;
		}
	}

	void addFieldsToDoc(NutchDocument doc, Metadata metadata) {
		for (Field field : config.getFields()) {
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
