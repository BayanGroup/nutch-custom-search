package ir.co.bayan.simorq.zal.nutch.extractor.engine;

/**
 * ExtractContext contains all information necessary for evaluation of extract expressions.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public abstract class ExtractContext {

	protected final String url;
	protected Object root;
	protected final ExtractEngine<ExtractContext> engine;

	public ExtractContext(ExtractEngine<ExtractContext> engine, String url) {
		this.engine = engine;
		this.url = url;
	}

	/**
	 * @return the engine
	 */
	public ExtractEngine<ExtractContext> getEngine() {
		return engine;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	public void setRoot(Object root) {
		this.root = root;
	}

	/**
	 * @return the root
	 */
	public Object getRoot() {
		return root;
	}

}
