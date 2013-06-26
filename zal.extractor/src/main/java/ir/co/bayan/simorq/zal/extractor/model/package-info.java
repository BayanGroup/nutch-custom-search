/**
 * Contains elements used during the extraction process. 
 * The provided config xml file is automatically maps to these elements using JAXB. 
 * Each element is responsible for extracting some part of data.
 */
@XmlSchema(namespace = "http://bayan.ir", elementFormDefault = XmlNsForm.QUALIFIED)
package ir.co.bayan.simorq.zal.extractor.model;

import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;

