package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.ExtractContext;

import java.net.MalformedURLException;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.Validate;
import org.apache.nutch.util.URLUtil;

/**
 * Resolves a possible relative url to absolute one based on the url in the context.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Resolve extends Function {

	@SuppressWarnings("unchecked")
	@Override
	public List<?> extract(Object root, ExtractContext context) throws Exception {
		Validate.isTrue(args != null && args.size() == 1, "Only one arg should be specified");

		List<String> res = (List<String>) args.get(0).extract(root, context);
		for (int i = 0; i < res.size(); i++) {
			String url = res.get(i);
			try {
				url = URLUtil.resolveURL(context.getUrl(), url).toString();
				res.set(i, url);
			} catch (MalformedURLException e) {
				// ignore
			}
		}
		return res;
	}

	@Override
	public String toString() {
		return "Resolve [" + super.toString() + "]";
	}

}
