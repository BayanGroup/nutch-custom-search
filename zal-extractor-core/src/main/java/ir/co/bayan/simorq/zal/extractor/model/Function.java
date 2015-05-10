package ir.co.bayan.simorq.zal.extractor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;

/**
 * A function, transforms its inputs (which is a list or a list of args) to a list of results. It operates in a context
 * and may have several arguments.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
public abstract class Function implements Extractor {

	@XmlElementRef
	protected List<Function> args;

	/**
	 * @return the args
	 */
	public List<Function> getArgs() {
		return args;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(List<Function> args) {
		this.args = args;
	}

	@Override
	public String toString() {
		return "args=" + args;
	}

}
