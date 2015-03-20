package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.process.Processor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

/**
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ProcessorDef {

	@XmlAttribute
	@XmlID
	private String name;

	private String clazz;

	private Processor processorInstance;

	public String getName() {
		return name;
	}

	@XmlAttribute(name = "class", required = true)
	public void setClazz(String clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		this.clazz = clazz;
		processorInstance = (Processor) Class.forName(clazz).newInstance();
	}

	/**
	 * @return the clazz
	 */
	public String getClazz() {
		return clazz;
	}

	/**
	 * @return the processorInstance
	 */
	public Processor getProcessorInstance() {
		return processorInstance;
	}

}
