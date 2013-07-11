package ir.co.bayan.simorq.zal.extractor.convert;

/**
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class PerisanConvertUtils {

	private static final String PERSIAN_NUMS = "" + '\u06F0' + '\u06F1' + '\u06F2' + '\u06F3' + '\u06F4' + '\u06F5'
			+ '\u06F6' + '\u06F7' + '\u06F8' + '\u06F9';

	public static String covertToPersianNumbers(String value) {
		StringBuffer buf = new StringBuffer(value.length());
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			int offset = (ch) - '0';
			if (offset < 10 && offset >= 0) {
				ch = PERSIAN_NUMS.charAt(offset);
			}
			buf.append(ch);
		}
		return buf.toString();
	}

	public static int convertYear(String value) {
		value = value.trim();
		switch (value.length()) {
		case 2:
			value = "13" + value;
		case 4:
			break;
		default:
			throw new RuntimeException("year length is not correct");
		}
		return Integer.parseInt(value);
	}

	public static int convertMonth(String value) {
		value = value.trim();
		return Integer.parseInt(value);
	}

	public static int convertDay(String value) {
		value = value.trim();
		return Integer.parseInt(value);

	}

}
