package ir.co.bayan.simorq.zal.nutch.selector.convert;

/**
 * Converts a canonical string to a specific type.
 * @author ali
 * @author taha
 *
 */
public interface Converter {
	
	/**
	 * @param value the value that should be converted. It might be null, or not be
	 * in canonical format. 
	 * @return if conversion is failed, the converter should return a default value.
	 */
	Object convert(String value);	

}
