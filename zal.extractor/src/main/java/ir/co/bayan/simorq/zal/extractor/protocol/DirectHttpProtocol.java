package ir.co.bayan.simorq.zal.extractor.protocol;

import ir.co.bayan.simorq.zal.extractor.core.Content;
import ir.co.bayan.simorq.zal.extractor.core.ExtractUtil;
import ir.co.bayan.simorq.zal.extractor.protocol.ProtocolException.ProtocolErrorCode;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.net.protocols.HttpDateFormat;
import org.apache.nutch.util.EncodingDetector;
import org.apache.nutch.util.MimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Uses the java.net for implementing the http protocol.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class DirectHttpProtocol implements Protocol {

	private static final Logger LOGGER = LoggerFactory.getLogger(DirectHttpProtocol.class);

	public static final String PARAM_PARAMETERS = "parameters";
	public static final String PARAM_METHOD = "method";

	private String proxyHost;
	private int proxyPort;
	private Integer connectTimeout;
	private Integer readTimeout;
	private String userAgent;
	private String acceptLanguage;
	private String acceptEncoding;
	private String accept;

	@Override
	public void setConf(Configuration conf) {
		proxyHost = conf.get("http.proxy.host");
		proxyPort = conf.getInt("http.proxy.port", 8080);
		connectTimeout = conf.getInt("http.timeout", 10000);
		readTimeout = conf.getInt("http.timeout", 10000);
		userAgent = getAgentString(conf.get("http.agent.name"), conf.get("http.agent.version"),
				conf.get("http.agent.description"), conf.get("http.agent.url"), conf.get("http.agent.email"));
		acceptLanguage = conf.get("http.accept.language", "en-us,en-gb,en;q=0.7,*;q=0.3");
		acceptEncoding = conf.get("http.accept.encoding", "x-gzip, gzip, deflate");
		accept = conf.get("http.accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	}

	// Copied from nutch HttpBase class
	private static String getAgentString(String agentName, String agentVersion, String agentDesc, String agentURL,
			String agentEmail) {

		if ((agentName == null) || (agentName.trim().length() == 0)) {
			// TODO : NUTCH-258
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("No User-Agent string set (http.agent.name)!");
			}
		}

		StringBuffer buf = new StringBuffer();

		buf.append(agentName);
		if (agentVersion != null) {
			buf.append("/");
			buf.append(agentVersion);
		}
		if (((agentDesc != null) && (agentDesc.length() != 0)) || ((agentEmail != null) && (agentEmail.length() != 0))
				|| ((agentURL != null) && (agentURL.length() != 0))) {
			buf.append(" (");

			if ((agentDesc != null) && (agentDesc.length() != 0)) {
				buf.append(agentDesc);
				if ((agentURL != null) || (agentEmail != null))
					buf.append("; ");
			}

			if ((agentURL != null) && (agentURL.length() != 0)) {
				buf.append(agentURL);
				if (agentEmail != null)
					buf.append("; ");
			}

			if ((agentEmail != null) && (agentEmail.length() != 0))
				buf.append(agentEmail);

			buf.append(")");
		}
		return buf.toString();
	}

	@Override
	public Content fetch(URL url, Map<String, Object> parameters) throws ProtocolException {
		Validate.notNull(url);
		Validate.notNull(parameters);

		InputStream is = null;
		try {
			HttpURLConnection connection = getConnection(url, parameters);
			is = getInputStream(connection, parameters);
			byte[] data = IOUtils.toByteArray(is);

			String contentType = connection.getContentType();
			String encoding = EncodingDetector.parseCharacterEncoding(contentType);
			if (StringUtils.isEmpty(encoding))
				encoding = ExtractUtil.sniffCharacterEncoding(data);

			return new Content(url, new ByteArrayInputStream(data), encoding, MimeUtil.cleanMimeType(contentType));
		} catch (IOException e) {
			throw new ProtocolException(ProtocolErrorCode.UNREACHABLE, e);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.error("Exception occured", e);
				}
		}
	}

	protected HttpURLConnection getConnection(URL url, Map<String, Object> parameters) throws IOException {
		HttpURLConnection connection;
		if (proxyHost != null) {
			Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
			connection = (HttpURLConnection) url.openConnection(proxy);
		} else
			connection = (HttpURLConnection) url.openConnection();

		// set timeouts
		if (connectTimeout != null)
			connection.setConnectTimeout(connectTimeout);
		if (readTimeout != null)
			connection.setReadTimeout(readTimeout);

		// headers
		if (userAgent != null)
			connection.setRequestProperty("User-Agent", userAgent);

		if (acceptLanguage != null)
			connection.setRequestProperty("Accept-Language", acceptLanguage);

		if (acceptEncoding != null)
			connection.setRequestProperty("Accept-Encoding", acceptEncoding);

		if (accept != null)
			connection.setRequestProperty("Accept", accept);

		// Set the last modified time
		Long lastModified = (Long) parameters.get(PARAM_LAST_MODIFIED);
		if (lastModified != null)
			connection.setRequestProperty("If-Modified-Since", HttpDateFormat.toString(lastModified));

		// Set basic authentication
		if (url.getUserInfo() != null) {
			String basicAuth = "Basic " + new String(new Base64().encode(url.getUserInfo().getBytes()));
			connection.setRequestProperty("Authorization", basicAuth);
		}

		// set method
		String method = (String) parameters.get(PARAM_METHOD);
		connection.setRequestMethod(StringUtils.defaultString(method, "GET"));
		if ("POST".equals(method)) {
			String params = (String) parameters.get(PARAM_PARAMETERS);
			if (params != null) {
				connection.setDoOutput(true);
				DataOutputStream dos = null;
				try {
					dos = new DataOutputStream(connection.getOutputStream());
					dos.writeBytes(params);
				} catch (IOException e) {
					throw e;
				} finally {
					dos.close();
				}
			}
		}

		return connection;
	}

	protected InputStream getInputStream(HttpURLConnection connection, Map<String, Object> parameters)
			throws ProtocolException, IOException {
		connection.connect();
		InputStream is = connection.getInputStream();

		int code = connection.getResponseCode();
		if (code != HttpURLConnection.HTTP_OK) {
			ProtocolErrorCode errorCode = deriveErrorCode(code);
			throw new ProtocolException(errorCode);
		}

		String encoding = connection.getContentEncoding();
		if ("gzip".equals(encoding) || "x-gzip".equals(encoding))
			return new GZIPInputStream(is);
		else if ("deflate".equals(encoding)) {
			Inflater inflater = new Inflater(true);
			return new InflaterInputStream(is, inflater);
		}
		return is;
	}

	private ProtocolErrorCode deriveErrorCode(int code) {
		ProtocolErrorCode errorCode = ProtocolErrorCode.UNREACHABLE;
		if (code != -1) {
			int codeClass = code / 100;
			if (codeClass == 5)
				errorCode = ProtocolErrorCode.UNAVAILABLE;
			else if (code == HttpURLConnection.HTTP_NOT_FOUND)
				errorCode = ProtocolErrorCode.NOT_FOUND;
			else if (codeClass == 4)
				errorCode = ProtocolErrorCode.ACCESS_DENIED;
			else if (codeClass == HttpURLConnection.HTTP_NOT_MODIFIED)
				errorCode = ProtocolErrorCode.NOT_CHANGED;
			else if (codeClass == 3)
				errorCode = ProtocolErrorCode.MOVED;
		}
		return errorCode;
	}
}
