package ir.co.bayan.simorq.zal.nutch.extractor.engine;

import ir.co.bayan.simorq.zal.nutch.extractor.ExtractedDoc;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class CssEngine extends ExtractEngine {

	@Override
	public List<ExtractedDoc> extractDocuments(Document document, String url, byte[] content, String encoding,
			String contentType) throws Exception {

		org.jsoup.nodes.Document parsedDoc = Jsoup.parse(new String(content, encoding));

		List<ExtractedDoc> result = new ArrayList<>();
		if (document.getPartitionBy() != null) {
			for (Element element : parsedDoc.select(document.getPartitionBy())) {
				ExtractedDoc extractedDoc = extractDocument(document, url, element);
				String id = extractedDoc.getFields().remove("id");
				extractedDoc.setUrl(id);
				result.add(extractedDoc);
			}
		} else {
			ExtractedDoc extractedDoc = extractDocument(document, url, parsedDoc);
			result.add(extractedDoc);
		}
		return result;
	}

	private ExtractedDoc extractDocument(Document document, String url, Element element) throws Exception {
		CssContext context = new CssContext(url);
		context.setRoot(element);
		ExtractedDoc extractedDoc = new ExtractedDoc(new HashMap<String, String>(
				document.getExtractTos().size() * 2 + 1), null);
		extractDocument(document, extractedDoc, context);
		return extractedDoc;
	}
}
