package ir.co.bayan.simorq.zal.extractor.protocol;

import ir.co.bayan.simorq.zal.extractor.core.Content;

import java.net.URL;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;

/**
 * Fetches a content according to a specific protocol. Protocol is thread-safe and it works only in sync mode. Each
 * protocol implementation should retrieve its protocol specific configurations such as timeout, proxy,.. from the
 * configuration files.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public interface Protocol {

	public static final String PARAM_LAST_MODIFIED = "lastModified";

	/**
	 * Protocol should read its configuration data from provided conf upon calling this method.
	 */
	void setConf(Configuration conf);

	/**
	 * Fetches the resource available in the specified url. Use parameters for adjusting protocol specific settings.
	 */
	Content fetch(URL url, Map<String, Object> parameters) throws ProtocolException;

}
