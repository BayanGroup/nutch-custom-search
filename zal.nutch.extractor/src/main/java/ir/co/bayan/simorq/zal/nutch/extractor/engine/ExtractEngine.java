package ir.co.bayan.simorq.zal.nutch.extractor.engine;

import ir.co.bayan.simorq.zal.nutch.extractor.ExtractedDoc;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Document;
import ir.co.bayan.simorq.zal.nutch.extractor.config.ExtractTo;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public abstract class ExtractEngine<C extends ExtractContext> {

	public abstract Object evaluate(String value, C context) throws Exception;

	public abstract Object getAttribute(Object res, String name, C context) throws Exception;

	public abstract Object getText(Object res, C context) throws Exception;

	public List<ExtractedDoc> extractDocuments(Document document, String url, byte[] content, String encoding,
			String contentType) throws Exception {
		C context = createContext(url, content, encoding, contentType);
		List<?> roots = getRoots(document, context);
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

	protected abstract C createContext(String url, byte[] content, String encoding, String contentType)
			throws Exception;

	protected abstract List<?> getRoots(Document document, C context) throws Exception;

	protected void extractDocument(Document document, ExtractedDoc extractedDoc, C context) throws Exception {
		Document parent = document.getInherits();
		if (parent != null) {
			extractDocument(parent, extractedDoc, context);
		}

		for (ExtractTo extractTo : document.getExtractTos()) {
			Field field = extractTo.getField();
			if (field != null) {
				String fieldValue = String.valueOf(extractTo.getValue().extract(context));
				extractedDoc.addField(field.getName(), fieldValue);
			}
		}
	}

}
