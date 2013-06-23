package ir.co.bayan.simorq.zal.nutch.extractor.config;

import ir.co.bayan.simorq.zal.nutch.extractor.ExtractedDoc.LinkData;
import ir.co.bayan.simorq.zal.nutch.extractor.engine.ExtractContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.nutch.util.URLUtil;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Link {

	private static final String TEXT_FUNC = ".text()";

	@XmlAttribute(required = true)
	private String url;

	@XmlAttribute(required = true)
	private String anchor;

	@XmlElementRef
	private List<Expr> expressions;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the anchor
	 */
	public String getAnchor() {
		return anchor;
	}

	/**
	 * @return the expressions
	 */
	public List<Expr> getExpressions() {
		return expressions;
	}

	public List<LinkData> extract(ExtractContext context) throws Exception {
		Validate.notNull(expressions, "At least one expr must provided");

		List<LinkData> res = new ArrayList<>();
		URL bae = new URL(context.getUrl());
		for (Expr expr : expressions) {
			List<?> nodes = expr.extract(context);
			List<String> urls = getValue(context, url, nodes);
			Iterator<String> anchors = getValue(context, anchor, nodes).iterator();
			for (String url : urls) {
				String anchor = anchors.next();
				if (!StringUtils.isEmpty(url) && !StringUtils.isEmpty(anchor)) {
					try {
						url = URLUtil.resolveURL(bae, url).toString();
						LinkData link = new LinkData(url, anchor.trim());
						res.add(link);
					} catch (MalformedURLException e) {
						// ignore
					}
				}
			}
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	private List<String> getValue(ExtractContext context, String target, List<?> nodes) throws Exception {
		if (TEXT_FUNC.equals(target)) {
			return (List<String>) context.getEngine().getText(context, nodes);
		} else
			return (List<String>) context.getEngine().getAttribute(context, nodes, target);
	}
}
