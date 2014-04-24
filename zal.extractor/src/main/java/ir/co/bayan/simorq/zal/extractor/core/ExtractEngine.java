package ir.co.bayan.simorq.zal.extractor.core;

import ir.co.bayan.simorq.zal.extractor.evaluation.CssEvaluator;
import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext;
import ir.co.bayan.simorq.zal.extractor.evaluation.Evaluator;
import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluatorFactory;
import ir.co.bayan.simorq.zal.extractor.evaluation.XPathEvaluator;
import ir.co.bayan.simorq.zal.extractor.model.Document;
import ir.co.bayan.simorq.zal.extractor.model.ExtractorConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extracts parts of an HTML or XML file based on the defined extract rules in the provided config file. Note that
 * although the configuration file has the types section, this class does not perform any type specific actions such as
 * type conversions. This class is thread-safe and you can configure it only once.
 * 
 * @see XPathEvaluator
 * @see CssEvaluator
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractEngine {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtractEngine.class);

	private ExtractorConfig extractorConfig;
	private Map<String, Document> docById;

	private static ExtractEngine instance;

	/**
	 * @return the instance
	 */
	public static ExtractEngine getInstance() {
		if (instance == null)
			instance = new ExtractEngine();
		return instance;
	}

	public ExtractEngine() {
	}

	public ExtractEngine(ExtractorConfig extractorConfig) throws Exception {
		setConf(extractorConfig);
	}

	public void setConf(ExtractorConfig extractorConfig) throws Exception {
		Validate.notNull(extractorConfig);
		// One time configuration
		if (this.extractorConfig != null)
			return;

		this.extractorConfig = extractorConfig;
		docById = new HashMap<>(extractorConfig.getDocuments().size() * 2 + 1);
		for (Document doc : extractorConfig.getDocuments()) {
			if (doc.getId() != null) {
				docById.put(doc.getId(), doc);
			}
		}

		// Validation: Checks that engine is not changed along the hierarchy of documents
		for (Document doc : extractorConfig.getDocuments()) {
			// All parents should either declare the same engine or no engine
			checkParentEngine(deriveEngineName(doc), doc.getInherits());
		}

	}

	private void checkParentEngine(String docEngine, Document parent) {
		if (parent == null)
			return;
		Validate.isTrue(StringUtils.isEmpty(parent.getEngine()) || parent.getEngine().equals(docEngine),
				"Engine can not be changed along the hierarchy: " + parent.getId());
		checkParentEngine(docEngine, parent.getInherits());
	}

	/**
	 * Extracts parts of the given content based on the defined extract-to rules in the config file. It uses the first
	 * matching document with the given url as the document that defines those extract-to rules.
	 * 
	 * @return a map of field names to the extracted value for that field according to the last last extract-to rule
	 *         that matches the field name. If no document matches the given url, null will be returned.
	 */
	public List<ExtractedDoc> extract(Content content) throws Exception {
		Validate.notNull(content);

		// 1. Decide on which document matches the url and contentType
		Document document = findMatchingDoc(content.getUrl().toString(), content.getType());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Matched document with url={} and contentType={} is {}", content.getUrl().toString(),
					content.getType());
		}
		if (document == null) {
			return null;
		}

		// 2. Select an engine for parsing the document
		String engine = deriveEngineName(document);
		Evaluator<? extends EvaluationContext> evalEngine = EvaluatorFactory.getInstance().getEvaluator(engine);
		if (evalEngine == null)
			throw new IllegalArgumentException("No engine found with the name " + engine);

		// 3. Parse the document and start the extraction process
		EvaluationContext context = evalEngine.createContext(content);
		return document.extract(context);
	}

	private String deriveEngineName(Document document) {
		return StringUtils.defaultIfEmpty(document.getInheritedEngine(), extractorConfig.getDefaultEngine());
	}

	public Document findMatchingDoc(String url, String contentType) {
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
