package ir.co.bayan.simorq.zal.nutch.extractor;

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

	public CssEngine(Extractor extractor) {
		super(extractor);
	}

	@Override
	public List<ExtractedDoc> extractDocuments(Document document, String content, String url) {

		org.jsoup.nodes.Document parsedDoc = Jsoup.parse(content);

		List<ExtractedDoc> result = new ArrayList<>();
		if (document.getPartitionBy() != null) {
			for (Element element : parsedDoc.select(document.getPartitionBy())) {
				CssContext context = new CssContext(url);
				context.setRoot(element);
				ExtractedDoc extractedDoc = new ExtractedDoc(new HashMap<String, String>(document.getExtractTos()
						.size() * 2 + 1), null);
				extractDocument(document, extractedDoc, context);
				String id = extractedDoc.getFields().remove("id");
				extractedDoc.setUrl(id);
				result.add(extractedDoc);
			}
		} else {
			CssContext context = new CssContext(url);
			context.setRoot(parsedDoc);
			ExtractedDoc extractedDoc = new ExtractedDoc(new HashMap<String, String>(
					document.getExtractTos().size() * 2 + 1), context.getUrl());
			extractDocument(document, extractedDoc, context);
			result.add(extractedDoc);
		}
		return result;
	}
}
