package ir.co.bayan.simorq.zal.extractor.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.apache.commons.io.IOUtils;

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

	public Content(URL url, InputStream data, String encoding, String contentType) throws IOException {
		super();
		this.url = url;
		this.data = IOUtils.toByteArray(data);
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
		return new ByteArrayInputStream(data);
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
