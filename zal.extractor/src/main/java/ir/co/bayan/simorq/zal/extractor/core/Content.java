package ir.co.bayan.simorq.zal.extractor.core;

import java.io.InputStream;
import java.net.URL;

/**
 * Represents a content fetched from specific url.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Content {

	private InputStream data;
	private String encoding;
	private String type;
	private URL url;

	public Content(URL url, InputStream data, String encoding, String contentType) {
		super();
		this.url = url;
		this.data = data;
		this.encoding = encoding;
		this.type = contentType;
	}

	/**
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * @return the data
	 */
	public InputStream getData() {
		return data;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @return the contentType
	 */
	public String getType() {
		return type;
	}

}
