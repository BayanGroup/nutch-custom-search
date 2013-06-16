package ir.co.bayan.simorq.zal.nutch.extractor.config;

import ir.co.bayan.simorq.zal.nutch.extractor.engine.ExtractContext;

import java.util.Arrays;
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
	public List<?> extract(ExtractContext context) throws Exception {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < args.size(); i++) {
			List<?> list = args.get(i).extract(context);
			for (int j = 0; j < list.size(); j++) {
				res.append(list.get(j));
				if (j < list.size() - 1)
					res.append(delimiter);
			}
			if (i < args.size() - 1)
				res.append(delimiter);
		}
		return Arrays.asList(res.toString());
	}

}
