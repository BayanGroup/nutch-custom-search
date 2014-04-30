package ir.co.bayan.simorq.zal.extractor.protocol;

import ir.co.bayan.simorq.zal.extractor.core.Content;
import ir.co.bayan.simorq.zal.extractor.protocol.ProtocolException.ProtocolErrorCode;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.hadoop.conf.Configuration;

/**
 * Reads from local file system.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class FileProtocol implements Protocol {

	public static final String PARAM_CONTENT_TYPE = "contentType";
	public static final String PARAM_ENCODING = "encoding";

	private String defaultEncoding;
	private String defaultContentType;

	@Override
	public void setConf(Configuration conf) {
		defaultEncoding = conf.get("watcher.file.encoding", "UTF-8");
		defaultContentType = conf.get("wathcer.file.contnetType", "application/text");
	}

	@Override
	public Content fetch(URL url, Map<String, Object> parameters) throws ProtocolException {
		Validate.notNull(url);
		Validate.notNull(parameters);

		try {
			File path = new File(url.toURI());
			// Checks whether the file is changed since the last modified time
			Long lastModified = (Long) parameters.get(PARAM_LAST_MODIFIED);
			if (lastModified != null) {
				long fileTime = path.lastModified();
				if (lastModified >= fileTime) {
					throw new ProtocolException(ProtocolErrorCode.NOT_CHANGED);
				}
			}

			byte[] data = FileUtils.readFileToByteArray(path);
			String encoding = StringUtils.defaultString((String) parameters.get(PARAM_ENCODING), defaultEncoding);
			String contentType = StringUtils.defaultString((String) parameters.get(PARAM_CONTENT_TYPE),
					defaultContentType);

			return new Content(url, new ByteArrayInputStream(data), encoding, contentType);
		} catch (IOException e) {
			throw new ProtocolException(ProtocolErrorCode.UNREACHABLE, e);
		} catch (URISyntaxException e) {
			throw new ProtocolException(ProtocolErrorCode.UNREACHABLE, e);
		}
	}
}
