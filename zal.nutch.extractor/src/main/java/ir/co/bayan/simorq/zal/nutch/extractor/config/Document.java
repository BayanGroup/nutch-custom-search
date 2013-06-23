package ir.co.bayan.simorq.zal.nutch.extractor.config;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Document {

	private String url;
	private Pattern urlPattern;

	private String contentType;
	private Pattern contentTypePattern;

	@XmlElement(name = "extract-to")
	private List<ExtractTo> extractTos = new ArrayList<>();

	@XmlAttribute
	@XmlID
	private String id;

	@XmlAttribute
	@XmlIDREF
	private Document inherits;

	@XmlAttribute
	private String engine;

	@XmlElement
	private Filter filter;

	@XmlElement
	private Partition partition;

	@XmlElementWrapper(name = "outlinks")
	@XmlElement(name = "link")
	private List<Link> outlinks;

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

	public List<ExtractTo> getExtractTos() {
		return extractTos;
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
	 * @return the engine
	 */
	public String getEngine() {
		return engine;
	}

	/**
	 * @return the filter
	 */
	public Filter getFilter() {
		return filter;
	}

	/**
	 * @return the partition
	 */
	public Partition getPartition() {
		return partition;
	}

	/**
	 * @return the outlinks
	 */
	public List<Link> getOutlinks() {
		return outlinks;
	}

}
