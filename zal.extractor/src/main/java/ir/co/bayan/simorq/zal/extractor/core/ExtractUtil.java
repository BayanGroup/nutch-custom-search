package ir.co.bayan.simorq.zal.extractor.core;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * These code are copied from nutch or xpath-filter. Both are licensed under Apache 2.0.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractUtil {

	private static final int CHUNK_SIZE = 2000;
	private static Pattern metaPattern = Pattern.compile("<meta\\s+([^>]*http-equiv=(\"|')?content-type(\"|')?[^>]*)>",
			Pattern.CASE_INSENSITIVE);
	private static Pattern charsetPattern = Pattern.compile("charset=\\s*([a-z][_\\-0-9a-z]*)",
			Pattern.CASE_INSENSITIVE);

	public static boolean isHtml(String contentType) {
		return "text/html".equals(contentType) || "application/xhtml+xml".equals(contentType);
	}

	public static boolean isXml(String contentType) {
		return contentType != null && (contentType.contains("/xml") || contentType.contains("+xml"));
	}

	public static String sniffCharacterEncoding(byte[] content) {
		int length = content.length < CHUNK_SIZE ? content.length : CHUNK_SIZE;

		// We don't care about non-ASCII parts so that it's sufficient
		// to just inflate each byte to a 16-bit value by padding.
		// For instance, the sequence {0x41, 0x82, 0xb7} will be turned into
		// {U+0041, U+0082, U+00B7}.
		String str = "";
		try {
			str = new String(content, 0, length, Charset.forName("ASCII").toString());
		} catch (UnsupportedEncodingException e) {
			// code should never come here, but just in case...
			return null;
		}

		Matcher metaMatcher = metaPattern.matcher(str);
		String encoding = null;
		if (metaMatcher.find()) {
			Matcher charsetMatcher = charsetPattern.matcher(metaMatcher.group(1));
			if (charsetMatcher.find())
				encoding = new String(charsetMatcher.group(1));
		}

		return encoding;
	}

}
