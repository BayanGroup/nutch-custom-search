package ir.co.bayan.simorq.zal.extractor.protocol;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ProtocolException extends Exception {

	private static final long serialVersionUID = 1L;

	public enum ProtocolErrorCode {
		UNREACHABLE, /* We can't reach the server */
		UNAVAILABLE, /* Server has problems */
		MOVED, /* Resource is moved */
		ACCESS_DENIED, /* Resource is available but not accessible */
		NOT_FOUND, /* No resource with this specification exists */
		NOT_CHANGED /* Resource is not changed since the last time fetch */
	}

	private ProtocolErrorCode code;

	public ProtocolException(ProtocolErrorCode code) {
		super();
		this.code = code;
	}

	public ProtocolException(ProtocolErrorCode code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public ProtocolErrorCode getCode() {
		return code;
	}

}
