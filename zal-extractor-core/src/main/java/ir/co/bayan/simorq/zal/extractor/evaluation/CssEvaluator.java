package ir.co.bayan.simorq.zal.extractor.evaluation;

import ir.co.bayan.simorq.zal.extractor.core.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An implementation of ExtractEngine which uses jsoup for evaluating css 3 selectors.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class CssEvaluator implements Evaluator<CssContext> {

	@Override
	public CssContext createContext(Content content) throws Exception {
        org.jsoup.nodes.Document parsedDoc = Jsoup.parse(content.getData(), content.getEncoding(), content.getUrl()
                .toString());
        return new CssContext(this, content, parsedDoc);
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
		List<String> attrs = new ArrayList<String>(elements.size());
		for (Element element : elements) {
			attrs.add(element.attr(name));
		}
		return attrs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> getText(CssContext context, List<?> input) throws Exception {
		List<Element> elements = (List<Element>) input;
		List<String> texts = new ArrayList<String>(elements.size());
		for (Element element : elements) {
            texts.add(element.text());
		}
		return texts;
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<?> getRaw(CssContext context, List<?> input) throws Exception {
        List<Element> elements = (List<Element>) input;
        List<String> texts = new ArrayList<String>(elements.size());
        for (Element element : elements) {
            texts.add(element.html());
        }
        return texts;
    }

	@Override
	public String getName() {
		return "css";
	}
}