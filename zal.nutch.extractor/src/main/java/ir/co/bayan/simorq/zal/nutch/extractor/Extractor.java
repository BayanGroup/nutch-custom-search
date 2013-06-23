package ir.co.bayan.simorq.zal.nutch.extractor;

import ir.co.bayan.simorq.zal.nutch.extractor.ExtractedDoc.LinkData;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Document;
import ir.co.bayan.simorq.zal.nutch.extractor.config.ExtractTo;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Field;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Link;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Partition;
import ir.co.bayan.simorq.zal.nutch.extractor.config.ExtractorConfig;
import ir.co.bayan.simorq.zal.nutch.extractor.engine.CssEngine;
import ir.co.bayan.simorq.zal.nutch.extractor.engine.ExtractContext;
import ir.co.bayan.simorq.zal.nutch.extractor.engine.ExtractEngine;
import ir.co.bayan.simorq.zal.nutch.extractor.engine.XPathEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * Extracts parts of an HTML or XML file based on the defined extract to expressions rules in the provided config file.
 * Note that although the configuration file has the types section, this class does not perform any type specific
 * actions such as type conversions.
 * 
 * @see XPathEngine
 * @see CssEngine
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Extractor {

	public static final String XPATH_ENGINE = "xpath";
	public static final String CSS_ENGINE = "css";

	private final ExtractorConfig extractorConfig;
	private final Map<String, Document> docById;
	private final Map<String, ExtractEngine<? extends ExtractContext>> extractEngines;

	public Extractor(ExtractorConfig extractorConfig) throws Exception {
		Validate.notNull(extractorConfig);

		this.extractorConfig = extractorConfig;
		docById = new HashMap<>(extractorConfig.getDocuments().size() * 2 + 1);
		for (Document doc : extractorConfig.getDocuments()) {
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
		Document document = findMatchingDoc(url, contentType);
		if (document == null) {
			return null;
		}

		String engine = StringUtils.defaultIfEmpty(document.getEngine(), extractorConfig.getDefaultEngine());
		ExtractEngine<? extends ExtractContext> extractEngine = extractEngines.get(engine);
		if (extractEngine == null)
			throw new IllegalArgumentException("No engine found with name " + engine);

		return extractDocuments(document, extractEngine, url, content, encoding, contentType);
	}

	private Document findMatchingDoc(String url, String contentType) throws Exception {
		for (Document doc : extractorConfig.getDocuments()) {
			boolean matches = true;
			if (doc.getUrlPattern() != null) {
				matches = doc.getUrlPattern().matcher(url).matches();
			}
			if (matches && doc.getContentTypePattern() != null) {
				matches = doc.getContentTypePattern().matcher(contentType).matches();
			}
			if (matches)
				return doc;
		}
		return null;
	}

	public Document getDocById(String id) {
		return docById.get(id);
	}

	public List<ExtractedDoc> extractDocuments(Document document,
			ExtractEngine<? extends ExtractContext> extractEngine, String url, byte[] content, String encoding,
			String contentType) throws Exception {
		ExtractContext context = extractEngine.createContext(url, content, encoding, contentType);
		List<?> roots = getRoots(document.getPartition(), context);
		List<ExtractedDoc> res = new ArrayList<>(roots.size());
		for (Object root : roots) {
			context.setRoot(root);
			ExtractedDoc extractedDoc = new ExtractedDoc(new HashMap<String, String>(
					document.getExtractTos().size() * 2 + 1), url);
			extractDocument(document, extractedDoc, context);
			String id = extractedDoc.getFields().get("id");
			if (id != null)
				extractedDoc.setUrl(id);
			res.add(extractedDoc);
		}
		return res;
	}

	public static List<?> getRoots(Partition partition, ExtractContext context) throws Exception {
		if (partition == null) {
			return Arrays.asList(context.getRoot());
		} else {
			return partition.getExpr().extract(context);
		}
	}

	protected void extractDocument(Document document, ExtractedDoc extractedDoc, ExtractContext context)
			throws Exception {
		Document parent = document.getInherits();
		if (parent != null) {
			extractDocument(parent, extractedDoc, context);
		}

		for (ExtractTo extractTo : document.getExtractTos()) {
			Field field = extractTo.getField();
			if (field != null) {
				List<String> res = extractTo.extract(context);
				if (res != null) {
					StringBuilder fieldValue = new StringBuilder();
					join(fieldValue, res);
					extractedDoc.addField(field.getName(), fieldValue.toString());
				}
			}
		}

		if (document.getOutlinks() != null) {
			List<LinkData> linkDatas = extractedDoc.getOutlinks() == null ? new ArrayList<LinkData>() : extractedDoc
					.getOutlinks();
			for (Link link : document.getOutlinks()) {
				linkDatas.addAll(link.extract(context));
			}
			extractedDoc.setOutlinks(linkDatas);
		}
	}

	public static void join(StringBuilder res, List<?> items) {
		for (int i = 0; i < items.size(); i++) {
			Object item = items.get(i);
			if (item instanceof List) {
				join(res, (List<?>) item);
			} else
				res.append(item);
			if (i < items.size() - 1)
				res.append(' ');
		}
	}

}
