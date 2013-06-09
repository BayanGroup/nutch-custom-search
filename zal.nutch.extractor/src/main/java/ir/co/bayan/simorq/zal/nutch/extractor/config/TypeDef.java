package ir.co.bayan.simorq.zal.nutch.extractor.config;

import ir.co.bayan.simorq.zal.nutch.extractor.convert.Converter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

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
	public void setConverter(String converter) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
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
