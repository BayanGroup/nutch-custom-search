package ir.co.bayan.simorq.zal.extractor.protocol;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:taha.ghasemi@gmail.com">Taha Ghasemi</a>
 */
public class Config {

    private Map<String, Object> values = new HashMap<String, Object>();

    public void set(String key, Object value) {
        values.put(key, value);
    }

    public String get(String key) {
        return String.valueOf(values.get(key));
    }

    public String get(String key, String defaultValue) {
        return StringUtils.defaultIfEmpty(String.valueOf(values.get(key)), defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        Integer value = (Integer) values.get(key);
        return value != null? value : defaultValue;
    }
}
