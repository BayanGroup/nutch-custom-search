package ir.co.bayan.simorq.zal.nutch.extractor.config;

import ir.co.bayan.simorq.zal.nutch.extractor.Extractor;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.Validate;
import org.apache.hadoop.conf.Configuration;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement(name = "config")
@XmlType(propOrder = { "types", "fields", "documents" })
public class ExtractorConfig {

	private static final String FILE_CONFIG_KEY = "extractor.file";
	private static final String DEFATUL_CONFIG_FILE = "extractors.xml";

	@XmlAttribute
	private boolean omitNonMatching = true;

	@XmlAttribute
	private String defaultEngine = Extractor.CSS_ENGINE;

	@XmlElementWrapper(name = "types")
	@XmlElement(name = "type")
	private List<TypeDef> types;

	@XmlElementWrapper(name = "fields")
	@XmlElement(name = "field")
	private List<Field> fields;

	@XmlElementWrapper(name = "documents")
	@XmlElement(name = "document")
	private List<Document> documents;

	/**
	 * @return the list
	 */
	public List<Document> getDocuments() {
		return documents;
	}

	public List<TypeDef> getTypes() {
		return types;
	}

	public List<Field> getFields() {
		return fields;
	}

	public boolean isOmitNonMatching() {
		return omitNonMatching;
	}

	/**
	 * @return the defaultEngine
	 */
	public String getDefaultEngine() {
		return defaultEngine;
	}

	public static ExtractorConfig readConfig(Reader configReader) throws JAXBException {
		Validate.notNull(configReader);

		JAXBContext context = JAXBContext.newInstance(ExtractorConfig.class, Document.class, ExtractTo.class,
				Filter.class, Partition.class, Field.class, Function.class, Constant.class, TypeDef.class, Text.class,
				Attribute.class, Concat.class, Expr.class, Replace.class, Url.class, First.class, Last.class,
				Size.class, Matches.class, Link.class, FunctionHolder.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (ExtractorConfig) unmarshaller.unmarshal(configReader);
	}

	public static ExtractorConfig readConfig(Configuration configuration) throws UnsupportedEncodingException,
			JAXBException {
		Validate.notNull(configuration);

		String configFileName = configuration.get(FILE_CONFIG_KEY, DEFATUL_CONFIG_FILE);
		InputStreamReader configReader = new InputStreamReader(
				configuration.getConfResourceAsInputStream(configFileName), "UTF-8");
		return readConfig(configReader);
	}

}
