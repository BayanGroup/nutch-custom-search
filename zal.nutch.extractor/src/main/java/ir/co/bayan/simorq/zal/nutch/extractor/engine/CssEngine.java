package ir.co.bayan.simorq.zal.nutch.extractor.engine;

import ir.co.bayan.simorq.zal.nutch.extractor.config.Document;
import ir.co.bayan.simorq.zal.nutch.extractor.config.Partition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class CssEngine extends ExtractEngine<CssContext> {

	@Override
	protected CssContext createContext(String url, byte[] content, String encoding, String contentType)
			throws Exception {
		org.jsoup.nodes.Document parsedDoc = Jsoup.parse(new String(content, encoding));
		return new CssContext(this, url, parsedDoc);
	}

	@Override
	protected List<?> getRoots(Document document, CssContext context) throws Exception {
		Partition partition = document.getPartition();
		if (partition == null) {
			return Arrays.asList(context.getRoot());
		} else {
			return (Elements) partition.getExpr().extract(context);
		}
	}

	@Override
	public Object evaluate(String value, CssContext context) throws Exception {
		return context.getRoot().select(value);
	}

	@Override
	public Object getAttribute(Object res, String name, CssContext context) throws Exception {
		if (res instanceof Elements) {
			Elements elements = (Elements) res;
			List<String> attrs = new ArrayList<>(elements.size());
			for (Element element : elements) {
				attrs.add(element.attr(name));
			}
			return attrs;
		} else
			return ((Element) res).attr(name);

	}

	@Override
	public Object getText(Object res, CssContext context) throws Exception {
		if (res instanceof Elements) {
			Elements elements = (Elements) res;
			List<String> texts = new ArrayList<>(elements.size());
			for (Element element : elements) {
				texts.add(element.text());
			}
			return texts;
		} else
			return ((Element) res).text();
	}
}