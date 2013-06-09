package ir.co.bayan.simorq.zal.nutch.extractor.config;

import ir.co.bayan.simorq.zal.nutch.extractor.ExtractContext;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Constant extends FieldValue {

	@Override
	public void extract(ExtractContext context) {
		context.getResult().append(value);
	}

}
