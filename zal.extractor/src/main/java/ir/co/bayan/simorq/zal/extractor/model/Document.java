package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.core.ExtractedDoc;
import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger LOGGER = LoggerFactory.getLogger(Document.class);

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

	@XmlAttribute
	private boolean update = false;

	private String every;
	private Integer everyInMiliSecond;

	@XmlAttribute
	private boolean adaptive = true;

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
	 * @return the update
	 */
	public boolean isUpdate() {
		return update;
	}

	/**
	 * @return the every
	 */
	public String getEvery() {
		return every;
	}

	/**
	 * @param every
	 *            the fetch schedule
	 */
	@XmlAttribute
	public void setEvery(String every) {
		this.every = every;
		computeEveryInMiliSecond();
	}

	private void computeEveryInMiliSecond() {
		String every = getInheritedEvery();
		if (every == null)
			return;
		char unit = every.charAt(every.length() - 1);
		int amount = Integer.valueOf(every.substring(0, every.length() - 1));
		switch (unit) {
		case 'm':
			everyInMiliSecond = amount * 60 * 1000;
			break;
		case 'h':
			everyInMiliSecond = amount * 60 * 60 * 1000;
			break;
		case 'd':
			everyInMiliSecond = amount * 24 * 60 * 60 * 1000;
			break;
		default:
			throw new IllegalArgumentException("Unknown unit in " + every);
		}
	}

	public Integer getEveryInMiliSecond() {
		return everyInMiliSecond;
	}

	public String getInheritedEvery() {
		return getEvery(this);
	}

	private String getEvery(Document document) {
		if (document == null)
			return null;
		if (!StringUtils.isEmpty(document.getEvery())) {
			return document.getEvery();
		}
		return getEvery(document.getInherits());
	}

	/**
	 * Tries to find engine from document hierarchy
	 */
	public String getInheritedEngine() {
		return getEngine(this);
	}

	/**
	 * @return the adaptive
	 */
	public boolean isAdaptive() {
		return adaptive;
	}

	private String getEngine(Document document) {
		if (document == null)
			return null;
		if (!StringUtils.isEmpty(document.getEngine()))
			return document.getEngine();
		return getEngine(document.getInherits());
	}

	public List<ExtractedDoc> extract(EvaluationContext context) throws Exception {
		List<ExtractedDoc> res = new ArrayList<ExtractedDoc>();

		for (Object root : getRoots(context.getMainRoot(), context)) {
			ExtractedDoc mainDoc = new ExtractedDoc();
			context.setCurrentDoc(mainDoc);
			mainDoc.addField(Fragment.URL_FIELD, context.getContent().getUrl().toString());
			mainDoc.setUpdate(update);
			res.add(mainDoc);
			extract(this, mainDoc, res, root, context);
		}

		return res;
	}

	private void extract(Document document, ExtractedDoc mainDoc, List<ExtractedDoc> extractedDocs, Object root,
			EvaluationContext context) throws Exception {
		Document parent = document.getInherits();
		if (parent != null) {
			extract(parent, mainDoc, extractedDocs, root, context);
		}

		if (LOGGER.isDebugEnabled())
			LOGGER.debug(this.toString());
		document.extractFields(root, context, mainDoc);
		document.insertSpecialFields(context, mainDoc);
		document.extractOutlinks(root, context, mainDoc);

		if (document.getFragments() != null) {
			for (Fragment fragment : document.getFragments()) {
				extractedDocs.addAll(fragment.extract(context.getMainRoot(), context));
			}
		}

	}

	public boolean matches(String url, String contentType) {
		boolean matches = false;
		if (urlPattern != null) {
			matches = urlPattern.matcher(url).find();
		}
		if (!matches && contentTypePattern != null) {
			matches = contentTypePattern.matcher(contentType).find();
		}
		return matches;
	}

	@Override
	public String toString() {
		return "Document [url=" + url + ", contentType=" + contentType + ", id=" + id + ", inherits=" + inherits
				+ ", engine=" + engine + "]";
	}

}
