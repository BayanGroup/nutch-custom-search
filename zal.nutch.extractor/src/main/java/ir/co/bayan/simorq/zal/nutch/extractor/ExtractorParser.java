package ir.co.bayan.simorq.zal.nutch.extractor;

import ir.co.bayan.simorq.zal.nutch.extractor.config.ExtractorConfig;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.ParseData;
import org.apache.nutch.parse.ParseResult;
import org.apache.nutch.parse.ParseStatus;
import org.apache.nutch.parse.ParseText;
import org.apache.nutch.parse.Parser;
import org.apache.nutch.protocol.Content;
import org.apache.nutch.util.EncodingDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A parser based on the extractor engine.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractorParser implements Parser {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtractorParser.class);

	private Configuration conf;
	private Extractor extractor;
	private String defaultEncoding;

	@Override
	public Configuration getConf() {
		return conf;
	}

	@Override
	public void setConf(Configuration configuration) {
		this.conf = configuration;
		defaultEncoding = configuration.get("parser.character.encoding.default", "UTF-8");
		try {
			initConf();
		} catch (Exception e) {
			LOGGER.error("", e);
		}

	}

	private void initConf() throws Exception {
		extractor = new Extractor(ExtractorConfig.readConfig(conf));
	}

	@Override
	public ParseResult getParse(Content content) {
		ParseResult parseResult = new ParseResult(content.getUrl());
		try {
			List<ExtractedDoc> docs = extractor.extract(content.getUrl(), content.getContent(), getEncoding(content),
					content.getContentType());
			if (docs != null) {
				for (ExtractedDoc doc : docs) {
					ParseText parseText = new ParseText(doc.getText());
					ParseData parseData = getParseData(content, doc);
					parseResult.put(doc.getUrl(), parseText, parseData);
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Exception on parsing " + content.getUrl(), e);
		}
		return parseResult;
	}

	private String getEncoding(Content content) {
		EncodingDetector detector = new EncodingDetector(conf);
		detector.autoDetectClues(content, true);
		if (ExtractorUtil.isHtml(content.getContentType()))
			detector.addClue(ExtractorUtil.sniffCharacterEncoding(content.getContent()), "sniffed");
		String encoding = detector.guessEncoding(content, defaultEncoding);
		return encoding;
	}

	private ParseData getParseData(Content content, ExtractedDoc doc) throws MalformedURLException {
		Metadata parseMeta = new Metadata();
		for (Entry<String, String> entry : doc.getFields().entrySet()) {
			parseMeta.add(entry.getKey(), entry.getValue());
		}
		parseMeta.add(ExtractorIndexingFilter.MATCHED_DOC, "true");

		return new ParseData(ParseStatus.STATUS_SUCCESS, doc.getTitle(), doc.getOutlinksAsArray(),
				content.getMetadata(), parseMeta);
	}

}
