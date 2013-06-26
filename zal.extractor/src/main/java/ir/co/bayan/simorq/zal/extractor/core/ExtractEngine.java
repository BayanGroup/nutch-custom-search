package ir.co.bayan.simorq.zal.extractor.core;

import ir.co.bayan.simorq.zal.extractor.evaluation.CssEvaluator;
import ir.co.bayan.simorq.zal.extractor.evaluation.Evaluator;
import ir.co.bayan.simorq.zal.extractor.evaluation.ExtractContext;
import ir.co.bayan.simorq.zal.extractor.evaluation.XPathEvaluator;
import ir.co.bayan.simorq.zal.extractor.model.Document;
import ir.co.bayan.simorq.zal.extractor.model.ExtractorConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * Extracts parts of an HTML or XML file based on the defined extract rules in the provided config file. Note that
 * although the configuration file has the types section, this class does not perform any type specific actions such as
 * type conversions. This class is thread-safe.
 * 
 * @see XPathEvaluator
 * @see CssEvaluator
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractEngine {

	public static final String XPATH_ENGINE = "xpath";
	public static final String CSS_ENGINE = "css";

	private final ExtractorConfig extractorConfig;
	private final Map<String, Document> docById;
	private final Map<String, Evaluator<? extends ExtractContext>> evaluators;

	public ExtractEngine(ExtractorConfig extractorConfig) throws Exception {
		Validate.notNull(extractorConfig);

		this.extractorConfig = extractorConfig;
		docById = new HashMap<>(extractorConfig.getDocuments().size() * 2 + 1);
		for (Document doc : extractorConfig.getDocuments()) {
			if (doc.getId() != null) {
				docById.put(doc.getId(), doc);
			}
		}

		evaluators = new HashMap<>();
		evaluators.put(CSS_ENGINE, new CssEvaluator());
		evaluators.put(XPATH_ENGINE, new XPathEvaluator());
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

		// 1. Decide on which document matches the url and contentType
		Document document = findMatchingDoc(url, contentType);
		if (document == null) {
			return null;
		}

		// 2. Select an engine for parsing the document
		String engine = StringUtils.defaultIfEmpty(document.getInheritedEngine(), extractorConfig.getDefaultEngine());
		Evaluator<? extends ExtractContext> evalEngine = evaluators.get(engine);
		if (evalEngine == null)
			throw new IllegalArgumentException("No engine found with the name " + engine);

		// 3. Parse the document and start the extraction process
		ExtractContext context = evalEngine.createContext(url, content, encoding, contentType);
		return document.extract(context);
	}

	private Document findMatchingDoc(String url, String contentType) throws Exception {
		for (Document doc : extractorConfig.getDocuments()) {
			if (doc.matches(url, contentType))
				return doc;
		}
		return null;
	}

	public Document getDocById(String id) {
		return docById.get(id);
	}

}
