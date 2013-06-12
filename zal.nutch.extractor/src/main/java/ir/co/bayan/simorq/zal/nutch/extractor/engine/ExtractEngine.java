package ir.co.bayan.simorq.zal.nutch.extractor.engine;

import ir.co.bayan.simorq.zal.nutch.extractor.ExtractedDoc;
import ir.co.bayan.simorq.zal.nutch.extractor.Extractor;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Document;
import ir.co.bayan.simorq.zal.nutch.extractor.config.ExtractTo;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Field;
import ir.co.bayan.simorq.zal.nutch.extractor.config.FieldValue;

import java.util.List;

import org.apache.commons.lang3.Validate;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public abstract class ExtractEngine {

	private final Extractor extractor;

	public ExtractEngine(Extractor extractor) {
		this.extractor = extractor;
	}

	public abstract List<ExtractedDoc> extractDocuments(Document document, String content, String url);

	protected void extractDocument(Document document, ExtractedDoc extractedDoc, ExtractContext context) {
		if (document.getInherits() != null) {
			Document parent = extractor.getDocById(document.getInherits());
			Validate.notNull(parent,
					"Can not find the document defined in inherits section with id " + document.getInherits());

			extractDocument(parent, extractedDoc, context);
		}

		for (ExtractTo extractTo : document.getExtractTos()) {
			StringBuilder fieldValue = new StringBuilder();
			context.setResult(fieldValue);
			Field field = extractTo.getField();
			if (field != null) {
				extractField(extractTo, context);
				extractedDoc.addField(field.getName(), fieldValue.toString());
			}
		}
	}

	protected void extractField(ExtractTo extractTo, ExtractContext context) {
		if (extractTo.getValues() != null) {
			int i = 0;
			for (FieldValue value : extractTo.getValues()) {
				value.extract(context);
				if (i++ < extractTo.getValues().size() - 1)
					context.getResult().append(extractTo.getDelimiter());
			}
		}
	}

}
