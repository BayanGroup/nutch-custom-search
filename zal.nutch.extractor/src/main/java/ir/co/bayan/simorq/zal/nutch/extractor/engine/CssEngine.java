package ir.co.bayan.simorq.zal.nutch.extractor.engine;

import ir.co.bayan.simorq.zal.nutch.extractor.ExtractorUtil;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 * An implementation of ExtractEngine which uses jsoup for evaluating css 3 selectors.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class CssEngine implements ExtractEngine<CssContext> {

	@Override
	public CssContext createContext(String url, byte[] content, String encoding, String contentType) throws Exception {
		if (ExtractorUtil.isHtml(contentType)) {
			org.jsoup.nodes.Document parsedDoc = Jsoup.parse(new ByteArrayInputStream(content), encoding, url);
			return new CssContext(this, url, parsedDoc);
		}
		throw new RuntimeException("Only html is accepted in CssEngine but the given content type is " + contentType);
	}

	@Override
	public List<?> evaluate(CssContext context, String value) throws Exception {
		return context.getRoot().select(value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> getAttribute(CssContext context, List<?> input, String name) throws Exception {
		List<Element> elements = (List<Element>) input;
		List<String> attrs = new ArrayList<>(elements.size());
		for (Element element : elements) {
			attrs.add(element.attr(name));
		}
		return attrs;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> getText(CssContext context, List<?> input) throws Exception {
		List<Element> elements = (List<Element>) input;
		List<String> texts = new ArrayList<>(elements.size());
		for (Element element : elements) {
			texts.add(element.text());
		}
		return texts;
	}
}