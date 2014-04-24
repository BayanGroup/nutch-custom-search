package ir.co.bayan.simorq.zal.extractor.nutch;

import ir.co.bayan.simorq.zal.extractor.core.ExtractEngine;
import ir.co.bayan.simorq.zal.extractor.core.ExtractUtil;
import ir.co.bayan.simorq.zal.extractor.core.ExtractedDoc;
import ir.co.bayan.simorq.zal.extractor.model.ExtractorConfig;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.metadata.Nutch;
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
			ExtractEngine.getInstance().setConf(ExtractorConfig.readConfig(conf));
		} catch (Exception e) {
			LOGGER.error("", e);
		}

	}

	@Override
	public ParseResult getParse(Content content) {
		LOGGER.info("Parsing: " + content.getUrl());
		ParseResult parseResult = new ParseResult(content.getUrl());
		try {
			List<ExtractedDoc> docs = ExtractEngine.getInstance().extract(
					new ir.co.bayan.simorq.zal.extractor.core.Content(new URL(content.getUrl()), content.getContent(),
							getEncoding(content), content.getContentType()));
			if (docs != null) {
				for (ExtractedDoc doc : docs) {
					if (LOGGER.isDebugEnabled())
						LOGGER.debug("Parsed document: " + doc.toString());
					ParseText parseText = new ParseText(doc.getText());
					ParseData parseData = getParseData(content, doc);
					parseResult.put(doc.getUrl(), parseText, parseData);
					parseData.getContentMeta().set(Nutch.FETCH_TIME_KEY, "0");
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
		if (ExtractUtil.isHtml(content.getContentType()))
			detector.addClue(ExtractUtil.sniffCharacterEncoding(content.getContent()), "sniffed");
		String encoding = detector.guessEncoding(content, defaultEncoding);
		return encoding;
	}

	private ParseData getParseData(Content content, ExtractedDoc doc) throws MalformedURLException {
		Metadata parseMeta = new Metadata();
		for (Entry<String, Object> entry : doc.getFields().entrySet()) {
			Object value = entry.getValue();
			// Check if it is a multi value
			if (value instanceof List) {
				for (Object valueItem : (List<?>) value) {
					parseMeta.add(entry.getKey(), valueItem.toString());
				}
			} else
				parseMeta.add(entry.getKey(), value.toString());
		}
		parseMeta.add(ExtractorIndexingFilter.MATCHED_DOC, "true");
		if (doc.isUpdate())
			parseMeta.add(ExtractorIndexingFilter.UPDATE_DOC, "true");

		return new ParseData(ParseStatus.STATUS_SUCCESS, doc.getTitle(), doc.getOutlinksAsArray(),
				content.getMetadata(), parseMeta);
	}

}
