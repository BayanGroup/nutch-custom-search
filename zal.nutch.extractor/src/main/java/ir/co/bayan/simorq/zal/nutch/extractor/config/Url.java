package ir.co.bayan.simorq.zal.nutch.extractor.config;

import ir.co.bayan.simorq.zal.nutch.extractor.engine.ExtractContext;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Url extends Function {

	@Override
	public Object extract(ExtractContext context) throws Exception {
		return context.getUrl();
	}

}
