package ir.co.bayan.simorq.zal.nutch.extractor.config;

import ir.co.bayan.simorq.zal.nutch.extractor.engine.ExtractContext;

import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;

/**
 * A function, transforms from its inputs (which is a list or a list of args) to a list of results. It operates in a
 * context and can have several arguments.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public abstract class Function {

	@XmlElementRef
	protected List<Function> args;

	/**
	 * @return the args
	 */
	public List<Function> getArgs() {
		return args;
	}

	public abstract List<?> extract(ExtractContext context) throws Exception;

}
