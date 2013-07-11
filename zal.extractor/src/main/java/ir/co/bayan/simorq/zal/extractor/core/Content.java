package ir.co.bayan.simorq.zal.extractor.core;

import java.net.URL;

/**
 * Represents a content fetched from specific url.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class Content {

	private byte[] data;
	private String encoding;
	private String type;
	private URL url;

	public Content(URL url, byte[] data, String encoding, String contentType) {
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
	public byte[] getData() {
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
