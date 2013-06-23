package ir.co.bayan.simorq.zal.nutch.extractor.config;

import ir.co.bayan.simorq.zal.nutch.extractor.ExtractedDoc.LinkData;
import ir.co.bayan.simorq.zal.nutch.extractor.Extractor;
import ir.co.bayan.simorq.zal.nutch.extractor.engine.ExtractContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.StringUtils;
import org.apache.nutch.util.URLUtil;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Link {

	@XmlElement(name = "href", required = true)
	private FunctionHolder href;

	@XmlElement(name = "anchor", required = true)
	private FunctionHolder anchor;

	@XmlElement
	private Partition partition;

	public List<LinkData> extract(ExtractContext context) throws Exception {
		List<LinkData> res = new ArrayList<>();
		URL base = new URL(context.getUrl());
		List<?> links = Extractor.getRoots(partition, context);
		Object root = context.getRoot();
		try {
			for (Object linkRoot : links) {
				context.setRoot(linkRoot);
				addLinks(context, res, base);
			}
		} finally {
			context.setRoot(root);
		}

		return res;
	}

	private void addLinks(ExtractContext context, List<LinkData> res, URL base) throws Exception {
		List<String> hrefs = href.extract(context);
		@SuppressWarnings("unchecked")
		List<String> anchors = anchor == null ? Collections.EMPTY_LIST : anchor.extract(context);
		Iterator<String> anchorI = anchors.iterator();
		for (String href : hrefs) {
			if (!StringUtils.isBlank(href)) {
				String a = anchorI.hasNext() ? anchorI.next() : "";
				a = StringUtils.defaultIfBlank(a, href);
				try {
					href = URLUtil.resolveURL(base, href).toString();
					LinkData link = new LinkData(href, a.trim());
					res.add(link);
				} catch (MalformedURLException e) {
					// ignore
				}
			}
		}
	}

}
