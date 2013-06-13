package ir.co.bayan.simorq.zal.nutch.extractor.config;

import ir.co.bayan.simorq.zal.nutch.extractor.engine.ExtractContext;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.Validate;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Text extends Function {

	@Override
	public Object extract(ExtractContext context) throws Exception {
		Validate.isTrue(args != null && args.size() == 1, "Only one arg should be specified");
		Object res = args.get(0).extract(context);
		return context.getEngine().getText(res, context);
	}

}
