package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Concats its inputs by the provided delimiter.
 * 
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
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < args.size(); i++) {
			List<?> list = args.get(i).extract(root, context);
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
