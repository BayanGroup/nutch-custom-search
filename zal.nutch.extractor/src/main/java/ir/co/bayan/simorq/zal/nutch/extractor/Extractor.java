package ir.co.bayan.simorq.zal.nutch.extractor;

import ir.co.bayan.simorq.zal.nutch.extractor.config.Document;
import ir.co.bayan.simorq.zal.nutch.extractor.config.SelectorConfiguration;
import ir.co.bayan.simorq.zal.nutch.extractor.engine.CssEngine;
import ir.co.bayan.simorq.zal.nutch.extractor.engine.ExtractEngine;
import ir.co.bayan.simorq.zal.nutch.extractor.engine.XPathEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * Extracts parts of an html file based on the defined css selector based rules in the provided config file. Note that
 * although the config file has the types section, this class does not perform any type specific actions such as type
 * conversions.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Extractor {

	public static final String XPATH_ENGINE = "xpath";
	public static final String CSS_ENGINE = "css";

	private final SelectorConfiguration config;
	private final Map<String, Document> docById;
	private final Map<String, ExtractEngine> extractEngines;

	public Extractor(SelectorConfiguration config) throws Exception {
		Validate.notNull(config);

		this.config = config;
		docById = new HashMap<>(config.getDocuments().size() * 2 + 1);
		for (Document doc : config.getDocuments()) {
			if (doc.getId() != null) {
				docById.put(doc.getId(), doc);
			}
		}

		extractEngines = new HashMap<>();
		extractEngines.put(CSS_ENGINE, new CssEngine());
		extractEngines.put(XPATH_ENGINE, new XPathEngine());
	}

	/**
	 * Extracts parts of the given content based on the defined extract-to rules in the config file. It uses the first
	 * matching document with the given url as the document that defines those extract-to rules.
	 * 
	 * @return a map of field names to the extracted value for that field according to the last last extract-to rule
	 *         that matches the field name. If no document matches the given url, null will be returned.
	 */
	public List<ExtractedDoc> extract(String url, byte[] content, String encoding, String contentType) throws Exception {
		Validate.notNull(url);
		Validate.notNull(content);

		// First decide on which document matches the url
		Document document = findMatchingDoc(url);
		if (document == null) {
			return null;
		}

		String engine = StringUtils.defaultIfEmpty(document.getEngine(), config.getDefaultEngine());
		ExtractEngine extractEngine = extractEngines.get(engine);
		if (extractEngine == null)
			throw new IllegalArgumentException("No engine found with name " + engine);

		return extractEngine.extractDocuments(document, url, content, encoding, contentType);
	}

	private Document findMatchingDoc(String url) {
		for (Document doc : config.getDocuments()) {
			if (doc.getUrl() != null) {
				if (doc.getUrlPattern().matcher(url).matches()) {
					return doc;
				}
			}
		}
		return null;
	}

	public Document getDocById(String id) {
		return docById.get(id);
	}

}
