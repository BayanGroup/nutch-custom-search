package ir.co.bayan.simorq.zal.extractor.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 */
@XmlEnum
public enum MatchMode {

    @XmlEnumValue("single")
    SINGLE,
    @XmlEnumValue("multiple")
    MULTIPLE;

}
