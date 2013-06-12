package ir.co.bayan.simorq.zal.nutch.extractor.engine;

import ir.co.bayan.simorq.zal.nutch.extractor.ExtractedDoc;
import ir.co.bayan.simorq.zal.nutch.extractor.Extractor;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Document;

import java.util.List;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class XPathEngine extends ExtractEngine {

	public XPathEngine(Extractor extractor) {
		super(extractor);
	}

	@Override
	public List<ExtractedDoc> extractDocuments(Document document, String content, String url) {
		return null;
	}

}
