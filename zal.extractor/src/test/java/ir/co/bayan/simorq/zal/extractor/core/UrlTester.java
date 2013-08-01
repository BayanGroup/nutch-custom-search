package ir.co.bayan.simorq.zal.extractor.core;

import ir.co.bayan.simorq.zal.extractor.model.ExtractorConfig;
import ir.co.bayan.simorq.zal.extractor.protocol.Protocol;
import ir.co.bayan.simorq.zal.extractor.protocol.ProtocolFactory;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Given a url, this class extract its parts according to extractors.xml file.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class UrlTester {

	public static void main(String[] args) {
		try {
			testUrl("http://ashpazi.blog.ir/tag/%D8%BA%D8%B0%D8%A7%DB%8C%20%D8%B3%D9%86%D8%AA%DB%8C");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testUrl(String urlStr) throws Exception {
		InputStreamReader configReader = new InputStreamReader(UrlTester.class.getResourceAsStream("/extractors.xml"));
		ExtractorConfig extractorConfig = ExtractorConfig.readConfig(configReader);
		ExtractEngine engine = new ExtractEngine(extractorConfig);

		URL url = new URL(urlStr);
		Protocol protocol = ProtocolFactory.getInstance().getProtocol(url);
		Content content = protocol.fetch(url, new HashMap<String, Object>());

		List<ExtractedDoc> res = engine.extract(content);
		for (ExtractedDoc doc : res) {
			System.out.println(doc);
		}
	}

}
