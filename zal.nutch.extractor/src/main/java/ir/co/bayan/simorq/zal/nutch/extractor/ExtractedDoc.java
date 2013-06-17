package ir.co.bayan.simorq.zal.nutch.extractor;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.apache.nutch.parse.Outlink;

/**
 * Represents an extracted fragment from a given content (HTML or XML). It is possible that from a single content,
 * multiple fragments extracted, each will be identified by its URL.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractedDoc {

	public static class Link {
		private final String url;
		private final String anchor;

		public Link(String url, String anchor) {
			this.url = url;
			this.anchor = anchor;
		}

		/**
		 * @return the url
		 */
		public String getUrl() {
			return url;
		}

		/**
		 * @return the anchor
		 */
		public String getAnchor() {
			return anchor;
		}

	}

	private final Map<String, String> fields;
	private String url;
	private String title;
	private List<Link> outlinks;
	private String text;

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

	/**
	 * @return the outlinks
	 */
	public List<Link> getOutlinks() {
		return outlinks;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	public Outlink[] getOutlinksAsArray() throws MalformedURLException {
		Outlink[] outlinks;
		if (getOutlinks() == null)
			outlinks = new Outlink[0];
		else {
			outlinks = new Outlink[getOutlinks().size()];
			for (int i = 0; i < outlinks.length; i++) {
				Link link = getOutlinks().get(i);
				outlinks[i] = new Outlink(link.getUrl(), link.getAnchor());
			}
		}
		return outlinks;
	}

}
