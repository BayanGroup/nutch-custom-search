package ir.co.bayan.simorq.zal.nutch.extractor.config;

import ir.co.bayan.simorq.zal.nutch.extractor.engine.ExtractContext;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.Validate;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Size extends Function {

	@Override
	public Object extract(ExtractContext context) throws Exception {
		Validate.isTrue(args != null && args.size() == 1, "Only one arg should be specified");
		Object res = args.get(0).extract(context);
		Validate.isInstanceOf(List.class, res, "This function only operates on list");
		return ((List<?>) res).size();
	}
}
