package ir.co.bayan.simorq.zal.nutch.extractor;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractContext {

	private StringBuilder result;
	private final String url;

	public ExtractContext(String url) {
		this.url = url;
	}

	/**
	 * @return the res
	 */
	public StringBuilder getResult() {
		return result;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(StringBuilder result) {
		this.result = result;
	}

}
