package ir.co.bayan.simorq.zal.extractor.evaluation;

import ir.co.bayan.simorq.zal.extractor.core.Content;
import ir.co.bayan.simorq.zal.extractor.core.ExtractUtil;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 * An implementation of ExtractEngine which uses jsoup for evaluating css 3 selectors.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class CssEvaluator implements Evaluator<CssContext> {

	@Override
	public CssContext createContext(Content content) throws Exception {
		if (ExtractUtil.isHtml(content.getType())) {
			org.jsoup.nodes.Document parsedDoc = Jsoup.parse(new ByteArrayInputStream(content.getData()),
					content.getEncoding(), content.getUrl().toString());
			return new CssContext(this, content, parsedDoc);
		}
		throw new RuntimeException("Only html is accepted in CssEngine but the given content type is "
				+ content.getType());
	}

	@Override
	public List<?> evaluate(Object root, CssContext context, String value) throws Exception {
		if (".".equals(value)) {
			return Arrays.asList(root);
		}
		return ((Element) root).select(value);
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

	@Override
	public String getName() {
		return "css";
	}
}