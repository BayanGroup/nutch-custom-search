package ir.co.bayan.simorq.zal.nutch.extractor.engine;

import ir.co.bayan.simorq.zal.nutch.extractor.ExtractedDoc;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Document;
import ir.co.bayan.simorq.zal.nutch.extractor.config.ExtractTo;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Field;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Partition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public abstract class ExtractEngine<C extends ExtractContext> {

	public abstract List<?> evaluate(String value, C context) throws Exception;

	public abstract List<?> getAttribute(List<?> res, String name, C context) throws Exception;

	public abstract List<?> getText(List<?> res, C context) throws Exception;

	protected abstract C createContext(String url, byte[] content, String encoding, String contentType)
			throws Exception;

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

	protected List<?> getRoots(Document document, C context) throws Exception {
		Partition partition = document.getPartition();
		if (partition == null) {
			return Arrays.asList(context.getRoot());
		} else {
			return partition.getExpr().extract(context);
		}
	}

	protected void extractDocument(Document document, ExtractedDoc extractedDoc, C context) throws Exception {
		Document parent = document.getInherits();
		if (parent != null) {
			extractDocument(parent, extractedDoc, context);
		}

		for (ExtractTo extractTo : document.getExtractTos()) {
			Field field = extractTo.getField();
			if (field != null) {
				List<?> res = extractTo.getValue().extract(context);
				StringBuilder fieldValue = new StringBuilder();
				for (Object item : res)
					fieldValue.append(item);
				extractedDoc.addField(field.getName(), fieldValue.toString());
			}
		}
	}

}
