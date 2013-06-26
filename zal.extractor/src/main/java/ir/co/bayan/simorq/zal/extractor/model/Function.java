package ir.co.bayan.simorq.zal.extractor.model;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;

/**
 * A function, transforms its inputs (which is a list or a list of args) to a list of results. It operates in a context
 * and may have several arguments.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public abstract class Function implements Extractor {

	@XmlElementRef
	protected List<Function> args;

	/**
	 * @return the args
	 */
	public List<Function> getArgs() {
		return args;
	}

	@Override
	public String toString() {
		return (args != null ? "args=" + Arrays.deepToString(args.toArray()) : "");
	}

}
