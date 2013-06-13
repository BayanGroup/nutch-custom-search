package ir.co.bayan.simorq.zal.nutch.extractor.config;

import ir.co.bayan.simorq.zal.nutch.extractor.engine.ExtractContext;

import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;

/**
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

	public abstract Object extract(ExtractContext context) throws Exception;

}
