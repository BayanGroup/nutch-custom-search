package ir.co.bayan.simorq.zal.nutch.extractor;

import ir.co.bayan.simorq.zal.nutch.extractor.config.SelectorConfiguration;

import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.parse.ParseResult;
import org.apache.nutch.parse.Parser;
import org.apache.nutch.protocol.Content;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractorParser implements Parser {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtractorParser.class);

	private Configuration configuration;
	private Extractor extractor;
	private String defaultEncoding;

	@Override
	public Configuration getConf() {
		return configuration;
	}

	@Override
	public void setConf(Configuration configuration) {
		this.configuration = configuration;
		defaultEncoding = configuration.get("parser.character.encoding.default", "UTF-8");
		try {
			initConf();
		} catch (Exception e) {
			LOGGER.error("", e);
		}

	}

	private void initConf() throws Exception {
		extractor = new Extractor(SelectorConfiguration.readConfig(configuration));
	}

	@Override
	public ParseResult getParse(Content c) {
		return null;
	}

}
