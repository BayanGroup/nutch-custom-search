package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.core.ExtractedDoc.LinkData;
import ir.co.bayan.simorq.zal.extractor.evaluation.ExtractContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Link extends Rooted {

	@XmlElement(name = "href", required = true)
	private FunctionHolder href;

	@XmlElement(name = "anchor", required = true)
	private FunctionHolder anchor;

	@Override
	public List<?> extract(Object root, ExtractContext context) throws Exception {
		List<LinkData> res = new ArrayList<>();
		for (Object subRoot : getRoots(root, context)) {
			addLinks(subRoot, context, res);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	private void addLinks(Object root, ExtractContext context, List<LinkData> res) throws Exception {
		List<String> hrefs = href.extract(root, context);
		List<String> anchors = anchor == null ? Collections.EMPTY_LIST : anchor.extract(root, context);
		Iterator<String> anchorI = anchors.iterator();
		for (String href : hrefs) {
			if (!StringUtils.isBlank(href)) {
				String a = anchorI.hasNext() ? anchorI.next() : "";
				a = StringUtils.defaultIfBlank(a, href);
				LinkData link = new LinkData(href, a.trim());
				res.add(link);
			}
		}
	}

	@Override
	public String toString() {
		return "Link [href=" + href + ", anchor=" + anchor + "]";
	}

}
