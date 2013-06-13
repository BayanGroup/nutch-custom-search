package ir.co.bayan.simorq.zal.nutch.extractor.config;

import ir.co.bayan.simorq.zal.nutch.extractor.engine.ExtractContext;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Concat extends Function {

	@XmlAttribute
	private String delimiter = " ";

	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}

	@Override
	public Object extract(ExtractContext context) throws Exception {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < args.size(); i++) {
			Object extracted = args.get(i).extract(context);
			if (extracted instanceof List<?>) {
				List<?> list = (List<?>) extracted;
				for (int j = 0; j < list.size(); j++) {
					res.append(list.get(j));
					if (j < list.size() - 1)
						res.append(delimiter);
				}
			} else
				res.append(extracted);
			if (i < args.size() - 1)
				res.append(delimiter);
		}
		return res.toString();
	}

}
