package ir.co.bayan.simorq.zal.extractor.core;

import ir.co.bayan.simorq.zal.extractor.evaluation.*;
import ir.co.bayan.simorq.zal.extractor.model.Document;
import ir.co.bayan.simorq.zal.extractor.model.ExtractorConfig;
import ir.co.bayan.simorq.zal.extractor.model.MatchMode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Extracts parts of an HTML or XML file based on the defined extract rules in the provided config file. Note that
 * although the configuration file has the types section, this class does not perform any type specific actions such as
 * type conversions. This class is thread-safe and you can configure it only once.
 * Note: the API of this class is subject to change, till version 1.0 comes out.
 * 
 * @see XPathEvaluator
 * @see CssEvaluator
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 *
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
		docById = new HashMap<String, Document>(extractorConfig.getDocuments().size() * 2 + 1);
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
	 * Extracts parts of the given content based on the defined extract-to rules in the config file.
     * The extract-to rules are computed from the matching document(s) with the given url and content type.
	 * 
	 * @return List of extracted documents from the given content. Null if no document matched with the given content.
	 */
	public List<ExtractedDoc> extract(Content content) throws Exception {
		Validate.notNull(content);

		List<ExtractedDoc> res = new ArrayList<ExtractedDoc>();
		
		// 1. Decide on which document matches the url and contentType
		List<Document> documents = findMatchingDocs(content.getUrl().toString(), content.getType());
		if (documents == null || documents.isEmpty()) {
			return null;
		}
		for (Document document : documents) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Matched document with url={} and contentType={} is {}", content.getUrl().toString(),
						content.getType(), document);
			}

			// 2. Select an engine for parsing the document
			String engine = deriveEngineName(document);
			Evaluator<? extends EvaluationContext> evalEngine = EvaluatorFactory.getInstance().getEvaluator(engine);
			if (evalEngine == null)
				throw new IllegalArgumentException("No engine found with the name " + engine);
	
			// 3. Parse the document and start the extraction process
			EvaluationContext context = evalEngine.createContext(content);
			List<ExtractedDoc> thisRes = document.extract(context);
			res.addAll(thisRes);
		}
		return merge(res);
	}
	
	private List<ExtractedDoc> merge(List<ExtractedDoc> documents) {
		Validate.notNull(documents);

		// If we're not in multiple match mode, do nothing
		if (extractorConfig.getMatchMode() != MatchMode.MULTIPLE) return documents;
		
		List<ExtractedDoc> res = new ArrayList<ExtractedDoc>();
		
		for (ExtractedDoc document : documents) {
			ExtractedDoc foundDoc = null;
			for (ExtractedDoc doc : res) {
				if (document.getUrl().equalsIgnoreCase(doc.getUrl())) {
					foundDoc = doc; 
					break;
				}
			}
			if (foundDoc == null) {
				// This url isn't in our result set, so just add it
				res.add(document);
			} else {
				// This url is already in our results, so merge in the fields and outlinks
                foundDoc.mergeWith(document);
			}
		}
		
		return res;
	}

	private String deriveEngineName(Document document) {
		return StringUtils.defaultIfEmpty(document.getInheritedEngine(), extractorConfig.getDefaultEngine());
	}

	public List<Document> findMatchingDocs(String url, String contentType) {
		List<Document> res = new ArrayList<Document>();

		for (Document doc : extractorConfig.getDocuments()) {
			if (doc.matches(url, contentType)) {
				res.add(doc);
				if (extractorConfig.getMatchMode() != MatchMode.MULTIPLE
						|| doc.isStopProcessing()) break;
			}
		}
		return res;
	}

	public Document getDocById(String id) {
		return docById.get(id);
	}

}
