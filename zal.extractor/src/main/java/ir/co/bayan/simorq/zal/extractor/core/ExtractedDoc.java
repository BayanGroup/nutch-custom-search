package ir.co.bayan.simorq.zal.extractor.core;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
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

	public static class LinkData {
		private final String url;
		private final String anchor;

		public LinkData(String url, String anchor) {
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

		@Override
		public String toString() {
			return "LinkData [url=" + url + "]";
		}

	}

	private final Map<String, Object> fields = new HashMap<String, Object>();
	private String url;
	private String title;
	private List<LinkData> outlinks = new ArrayList<LinkData>();
	private String text;
	private boolean update;

	/**
	 * @return the fields
	 */
	public Map<String, Object> getFields() {
		return fields;
	}

	public Object getField(String name) {
		return fields.get(name);
	}

	public void addField(String name, Object value) {
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
	public List<LinkData> getOutlinks() {
		return outlinks;
	}

	/**
	 * @param outlinks
	 *            the outlinks to set
	 */
	public void setOutlinks(List<LinkData> outlinks) {
		this.outlinks = outlinks;
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

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the update
	 */
	public boolean isUpdate() {
		return update;
	}

	/**
	 * @param update
	 *            the update to set
	 */
	public void setUpdate(boolean update) {
		this.update = update;
	}

	public Outlink[] getOutlinksAsArray() throws MalformedURLException {
		Outlink[] res;
		if (outlinks == null)
			res = new Outlink[0];
		else {
			res = new Outlink[outlinks.size()];
			for (int i = 0; i < outlinks.size(); i++) {
				LinkData linkData = outlinks.get(i);
				res[i] = new Outlink(linkData.getUrl(), linkData.getAnchor());
			}
		}
		return res;
	}

	@Override
	public String toString() {
		return "ExtractedDoc [fields=" + fields + ", url=" + url + ", title=" + title + ", outlinks=" + outlinks + "]";
	}

}
