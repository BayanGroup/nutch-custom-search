package ir.co.bayan.simorq.zal.extractor.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.nutch.parse.Outlink;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	private Map<String, Object> fields = new HashMap<String, Object>();
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

    /**
     * Merge this document the given document with this document. The properties
     * of this document have priority over the given document.
     */
    public void mergeWith(ExtractedDoc document) {
        //merge fields
        Map<String, Object> existingFields = getFields();
        Map<String, Object> newFields = document.getFields();
        for (Entry<String, Object> newField: newFields.entrySet()) {
            String newKey = newField.getKey();
            Object newValue = newField.getValue();
            if (!existingFields.containsKey(newKey)) {
                addField(newKey, newValue);
            } else if (newValue instanceof List<?>) {
                try {
                    // Multi-value field, so append new to existing
                    @SuppressWarnings("unchecked")
                    List<String> existingField = (List<String>) existingFields.get(newKey);
                    @SuppressWarnings("unchecked")
                    List<String> newFieldList = (List<String>) newFields.get(newKey);
                    existingField.addAll(newFieldList);
                } catch (ClassCastException ex) {
                    // Ignore this - we'll stick with the original value(s)
                }
            }
        }

        //merge outlinks
        //TODO possible duplicates, no problem when using nutch though
        List<LinkData> existingLinks = getOutlinks();
        existingLinks.addAll(document.getOutlinks());
        setOutlinks(existingLinks);

        //merge nutch specific fields
        if(StringUtils.isEmpty(getTitle()))
            setTitle(document.getTitle());
        if(StringUtils.isEmpty(getText()))
            setText(document.getText());
    }

	@Override
	public String toString() {
		return "ExtractedDoc [fields=" + fields + ", url=" + url + ", title=" + title + ", outlinks=" + outlinks + "]";
	}

}
