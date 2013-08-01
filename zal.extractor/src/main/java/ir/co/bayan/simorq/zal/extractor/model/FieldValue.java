package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Returns the extracted value of the given field.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement(name = "field-value")
public class FieldValue extends Function {

	@XmlIDREF
	@XmlAttribute
	private Field field;

	public Field getField() {
		return field;
	}

	@Override
	public List<?> extract(Object root, EvaluationContext context) {
		return Arrays.asList(context.getCurrentDoc().getField(field.getName()));
	}

	@Override
	public String toString() {
		return "FieldValue [field=" + field + "]";
	}

}
