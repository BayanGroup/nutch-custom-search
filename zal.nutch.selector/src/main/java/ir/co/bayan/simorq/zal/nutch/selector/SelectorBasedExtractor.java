package ir.co.bayan.simorq.zal.nutch.selector;

import ir.co.bayan.simorq.zal.nutch.selector.config.Document;
import ir.co.bayan.simorq.zal.nutch.selector.config.ExtractTo;
import ir.co.bayan.simorq.zal.nutch.selector.config.Field;
import ir.co.bayan.simorq.zal.nutch.selector.config.FieldValue;
import ir.co.bayan.simorq.zal.nutch.selector.config.SelectorConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.Validate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 * Extracts parts of an html file based on the defined css selector based rules in the provided config file. Note that
 * although the config file has the types section, this class does not perform any type specific actions such as type
 * conversions.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class SelectorBasedExtractor {

	private final SelectorConfiguration config;
	private final Map<String, Document> docById;

	public SelectorBasedExtractor(SelectorConfiguration config) throws JAXBException {
		Validate.notNull(config);

		this.config = config;
		docById = new HashMap<>(config.getDocuments().size() * 2 + 1);
		for (Document doc : config.getDocuments()) {
			if (doc.getId() != null) {
				docById.put(doc.getId(), doc);
			}
		}
	}

	/**
	 * Extracts parts of the given content based on the defined extract-to rules in the config file. It uses the first
	 * matching document with the given url as the document that defines those extract-to rules.
	 * 
	 * @param contentType
	 *            TODO
	 * 
	 * @return a map of field names to the extracted value for that field according to the last last extract-to rule
	 *         that matches the field name. If no document matches the given url, null will be returned.
	 */
	public List<ExtractedDoc> extract(String url, String content, String contentType) throws IOException {
		Validate.notNull(url);
		Validate.notNull(content);

		// First decide on which document matches the url
		Document document = findMatchingDoc(url);
		if (document == null) {
			return null;
		}

		org.jsoup.nodes.Document parsedDoc = Jsoup.parse(content);

		return extractDocuments(document, parsedDoc, url);
	}

	private List<ExtractedDoc> extractDocuments(Document document, org.jsoup.nodes.Document parsedDoc, String url) {
		List<ExtractedDoc> result = new ArrayList<>();
		if (document.getSelector() != null) {
			for (Element element : parsedDoc.select(document.getSelector())) {
				ExtractedDoc extractedDoc = new ExtractedDoc(new HashMap<String, String>(document.getExtractTos()
						.size() * 2 + 1), url);
				extractDocument(document, element, url, extractedDoc);
				result.add(extractedDoc);
			}
		} else {
			ExtractedDoc extractedDoc = new ExtractedDoc(new HashMap<String, String>(
					document.getExtractTos().size() * 2 + 1), url);
			extractDocument(document, parsedDoc, url, extractedDoc);
			result.add(extractedDoc);
		}
		return result;
	}

	private void extractDocument(Document document, Element root, String url, ExtractedDoc result) {
		if (document.getInherits() != null) {
			Document parent = docById.get(document.getInherits());
			Validate.notNull(parent,
					"Can not find the document defined in inherits section with id " + document.getInherits());

			extractDocument(parent, root, url, result);
		}

		for (ExtractTo extractTo : document.getExtractTos()) {
			StringBuilder fieldValue = new StringBuilder();
			Field field = extractTo.getField();
			if (field != null) {
				extractField(extractTo, fieldValue, root, url);
				result.addField(field.getName(), fieldValue.toString());
			}
		}
	}

	private void extractField(ExtractTo extractTo, StringBuilder res, Element root, String url) {
		if (extractTo.getValues() != null) {
			int i = 0;
			for (FieldValue value : extractTo.getValues()) {
				value.extract(res, root, url);
				if (i++ < extractTo.getValues().size() - 1)
					res.append(extractTo.getDelimiter());
			}
		}
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

}
