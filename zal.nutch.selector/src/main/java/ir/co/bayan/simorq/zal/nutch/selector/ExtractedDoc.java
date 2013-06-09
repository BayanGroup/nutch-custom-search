package ir.co.bayan.simorq.zal.nutch.selector;

import java.util.Map;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractedDoc {

	private final Map<String, String> fields;
	private final String url;

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

}
