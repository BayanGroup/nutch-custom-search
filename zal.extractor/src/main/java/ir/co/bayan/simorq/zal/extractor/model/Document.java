package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.core.ExtractedDoc;
import ir.co.bayan.simorq.zal.extractor.evaluation.ExtractContext;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

import org.apache.commons.lang3.StringUtils;

/**
 * A document defines a set of extract-to rules to be used in the extraction process. These rules are evaluated using
 * the default evaluation engine specified in the document. Each document may contain several fragments (or itself may
 * contain root) which results to generation of multiple sub documents. Each document may inherit its extract-to,
 * engine, or fragments from another document. Documents are matched using their url or content type both can be regex.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Document extends Fragment {

	private String url;
	private Pattern urlPattern;

	private String contentType;
	private Pattern contentTypePattern;

	@XmlAttribute
	@XmlID
	private String id;

	@XmlAttribute
	@XmlIDREF
	private Document inherits;

	@XmlElement
	private Filter filter;

	@XmlElement(name = "fragment")
	private List<Fragment> fragments;

	@XmlAttribute
	private String engine;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the urlPattern
	 */
	public Pattern getUrlPattern() {
		return urlPattern;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	@XmlAttribute
	public void setUrl(String url) {
		this.url = url;
		this.urlPattern = Pattern.compile(url);
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @return the contentTypePatter
	 */
	public Pattern getContentTypePattern() {
		return contentTypePattern;
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
		this.contentTypePattern = Pattern.compile(contentType);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the inherits
	 */
	public Document getInherits() {
		return inherits;
	}

	/**
	 * @return the filter
	 */
	public Filter getFilter() {
		return filter;
	}

	/**
	 * @return the fragments
	 */
	public List<Fragment> getFragments() {
		return fragments;
	}

	public String getEngine() {
		return engine;
	}

	/**
	 * Tries to find engine from document hierarchy
	 */
	public String getInheritedEngine() {
		return getEngine(this);
	}

	private String getEngine(Document document) {
		if (document == null)
			return null;
		if (!StringUtils.isEmpty(document.getEngine()))
			return document.getEngine();
		return getEngine(document.getInherits());
	}

	public List<ExtractedDoc> extract(ExtractContext context) throws Exception {
		List<ExtractedDoc> res = new ArrayList<>();

		ExtractedDoc mainDoc = new ExtractedDoc();
		mainDoc.setUrl(url);
		res.add(mainDoc);

		extract(this, mainDoc, res, context);

		return res;
	}

	private void extract(Document document, ExtractedDoc mainDoc, List<ExtractedDoc> extractedDocs,
			ExtractContext context) throws Exception {
		Document parent = document.getInherits();
		if (parent != null) {
			extract(parent, mainDoc, extractedDocs, context);
		}

		document.extractFields(context.getRoot(), context, mainDoc);
		document.insertSpecialFields(mainDoc);
		document.extractOutlinks(context.getRoot(), context, mainDoc);

		if (document.getFragments() != null) {
			for (Fragment fragment : document.getFragments()) {
				extractedDocs.addAll(fragment.extract(context.getRoot(), context));
			}
		}

	}

	public boolean matches(String url, String contentType) {
		boolean matches = true;
		if (urlPattern != null) {
			matches = urlPattern.matcher(url).matches();
		}
		if (matches && contentTypePattern != null) {
			matches = contentTypePattern.matcher(contentType).matches();
		}
		return matches;
	}

}
