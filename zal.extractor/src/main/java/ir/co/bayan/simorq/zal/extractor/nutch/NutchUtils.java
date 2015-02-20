package ir.co.bayan.simorq.zal.extractor.nutch;

import ir.co.bayan.simorq.zal.extractor.model.ExtractorConfig;
import org.apache.commons.lang3.Validate;
import org.apache.hadoop.conf.Configuration;

import java.io.InputStreamReader;

/**
 * @author <a href="mailto:taha.ghasemi@gmail.com">Taha Ghasemi</a>
 */
public class NutchUtils {

    private static final String FILE_CONFIG_KEY = "extractor.file";
    private static final String DEFATUL_CONFIG_FILE = "extractors.xml";

    /**
     * Returns extractor config from the given nutch config.
     */
    public static ExtractorConfig config(Configuration configuration) throws Exception {
        Validate.notNull(configuration);

        String configFileName = configuration.get(FILE_CONFIG_KEY, DEFATUL_CONFIG_FILE);
        InputStreamReader configReader = new InputStreamReader(
                configuration.getConfResourceAsInputStream(configFileName), "UTF-8");
        return ExtractorConfig.readConfig(configReader);
    }
}
