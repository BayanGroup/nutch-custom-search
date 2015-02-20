package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.convert.Converter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

/**
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class TypeDef {

	@XmlAttribute
	@XmlID
	private String name;

	private String converter;

	private Converter converterInstance;

	public String getName() {
		return name;
	}

	@XmlAttribute
	public void setConverter(String converter) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		this.converter = converter;
		converterInstance = (Converter) Class.forName(converter).newInstance();
	}

	public String getConverter() {
		return converter;
	}

	public Converter getConverterInstance() {
		return converterInstance;
	}

}
