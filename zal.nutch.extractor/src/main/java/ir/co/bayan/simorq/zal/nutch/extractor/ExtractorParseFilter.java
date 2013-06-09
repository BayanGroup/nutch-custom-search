package ir.co.bayan.simorq.zal.nutch.extractor;

import ir.co.bayan.simorq.zal.nutch.extractor.config.SelectorConfiguration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.HTMLMetaTags;
import org.apache.nutch.parse.HtmlParseFilter;
import org.apache.nutch.parse.ParseResult;
import org.apache.nutch.protocol.Content;
import org.w3c.dom.DocumentFragment;

public class ExtractorParseFilter implements HtmlParseFilter {

	public static final String MATCHED_DOC = "matched-doc";

	private static final Logger logger = Logger.getLogger(ExtractorParseFilter.class);

	private Configuration configuration;
	private Extractor extractor;
	private String defaultEncoding;

	@Override
	public ParseResult filter(Content content, ParseResult parseResult, HTMLMetaTags metaTags,
			DocumentFragment documentFragment) {
		try {
			Metadata metadata = parseResult.get(content.getUrl()).getData().getParseMeta();
			String encoding = StringUtils.defaultString(metadata.get(Metadata.ORIGINAL_CHAR_ENCODING), defaultEncoding);
			String contentStr = new String(content.getContent(), encoding);
			List<ExtractedDoc> extractedDocs = extractor
					.extract(content.getUrl(), contentStr, content.getContentType());
			if (extractedDocs != null) {
				for (ExtractedDoc doc : extractedDocs) {
					Metadata docMetadata = parseResult.get(doc.getUrl()).getData().getParseMeta();
					// Indicates that this document is matched with one of urls defined in the config.
					// This will be used in ParseMetadataIndexingFilter to decide whether exclude document or not
					docMetadata.add(MATCHED_DOC, "true");
					for (Entry<String, String> entry : doc.getFields().entrySet()) {
						docMetadata.add(entry.getKey(), entry.getValue());
					}

				}
			}

		} catch (IOException e) {
			logger.warn("", e);
		}

		return parseResult;
	}

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
		} catch (UnsupportedEncodingException | JAXBException e) {
			logger.error("", e);
		}

	}

	private void initConf() throws UnsupportedEncodingException, JAXBException {
		extractor = new Extractor(SelectorConfiguration.readConfig(configuration));
	}

}
