package ir.co.bayan.simorq.zal.nutch.selector.config;

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
public class SelectorConfiguration {

	private static final String FILTER_CSS_FILE = "filter.css.file";
	private static final String CSS_PATHS_FILE = "css-paths.xml";

	@XmlAttribute
	private final boolean omitNonMatching = true;

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

	public static SelectorConfiguration readConfig(Reader configReader) throws JAXBException {
		Validate.notNull(configReader);

		JAXBContext context = JAXBContext.newInstance(SelectorConfiguration.class, Document.class, ExtractTo.class,
				Field.class, FieldValue.class, Constant.class, Content.class, TypeDef.class, Attribute.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (SelectorConfiguration) unmarshaller.unmarshal(configReader);
	}

	public static SelectorConfiguration readConfig(Configuration configuration) throws UnsupportedEncodingException,
			JAXBException {
		Validate.notNull(configuration);

		String configFileName = configuration.get(FILTER_CSS_FILE, CSS_PATHS_FILE);
		InputStreamReader configReader = new InputStreamReader(
				configuration.getConfResourceAsInputStream(configFileName), "UTF-8");
		return readConfig(configReader);
	}

}
