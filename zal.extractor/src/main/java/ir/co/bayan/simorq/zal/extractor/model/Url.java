package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.ExtractContext;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Returns the current url in the context.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Url extends Function {

	@Override
	public List<?> extract(Object root, ExtractContext context) throws Exception {
		return Arrays.asList(context.getUrl());
	}

	@Override
	public String toString() {
		return "Url []";
	}

}
