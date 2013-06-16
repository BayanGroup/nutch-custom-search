package ir.co.bayan.simorq.zal.nutch.extractor;

import java.util.Map;

/**
 * Represents an extracted fragment from a given content (HTML or XML). It is possible that from a single content,
 * multiple fragments extracted, each will be identified by its URL.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractedDoc {

	private final Map<String, String> fields;
	private String url;

	public ExtractedDoc(Map<String, String> fields, String url) {
		this.fields = fields;
		this.url = url;
	}

	/**
	 * @return the fields
	 */
	public Map<String, String> getFields() {
		return fields;
	}

	public void addField(String name, String value) {
		fields.put(name, value);
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
