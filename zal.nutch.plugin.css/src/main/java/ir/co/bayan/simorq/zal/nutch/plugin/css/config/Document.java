package ir.co.bayan.simorq.zal.nutch.plugin.css.config;

import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Document {

	private String url;
	private Pattern urlPattern;

	@XmlElement(name = "extract-to")
	private List<ExtractTo> extractTos;

	@XmlAttribute
	@XmlID
	private String id;

	@XmlAttribute
	private String inherits;

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
	public String getInherits() {
		return inherits;
	}

}
