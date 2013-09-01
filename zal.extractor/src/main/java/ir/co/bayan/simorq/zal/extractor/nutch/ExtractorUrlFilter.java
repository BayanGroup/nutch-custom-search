package ir.co.bayan.simorq.zal.extractor.nutch;

import ir.co.bayan.simorq.zal.extractor.core.ExtractEngine;
import ir.co.bayan.simorq.zal.extractor.model.ExtractorConfig;

import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.net.URLFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A filter that filter urls based on the whether they match in the extractors.xml file. .
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractorUrlFilter implements URLFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtractorUrlFilter.class);

	@Override
	public Configuration getConf() {
		return null;
	}

	@Override
	public void setConf(Configuration conf) {
		try {
			ExtractEngine.getInstance().setConf(ExtractorConfig.readConfig(conf));
		} catch (Exception e) {
			LOGGER.error("Exception occured", e);
		}
	}

	@Override
	public String filter(String urlString) {
		return ExtractEngine.getInstance().findMatchingDoc(urlString, null) == null ? null : urlString;
	}

}
