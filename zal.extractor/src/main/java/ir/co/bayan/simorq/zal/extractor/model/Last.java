package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.ExtractContext;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.Validate;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Last extends Function {

	@Override
	public List<?> extract(Object root, ExtractContext context) throws Exception {
		Validate.isTrue(args != null && args.size() == 1, "Only one arg should be specified");
		List<?> list = args.get(0).extract(root, context);
		return Arrays.asList(list.get(list.size() - 1));
	}

	@Override
	public String toString() {
		return "Last [" + super.toString() + "]";
	}
}
