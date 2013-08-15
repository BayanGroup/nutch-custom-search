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
			// testUrl("http://ashpazi.blog.ir/category/%D8%A2%D8%B1%D8%B4%DB%8C%D9%88%20%D8%BA%D8%B0%D8%A7%D9%87%D8%A7%DB%8C%20%D8%A7%DB%8C%D8%B1%D8%A7%D9%86%DB%8C/%D8%A7%D9%86%D9%88%D8%A7%D8%B9%20%D8%A2%D8%B4/%D8%A2%D8%B4%20%DA%AF%D9%88%D8%AC%D9%87%20%D9%81%D8%B1%D9%86%DA%AF%DB%8C%20%D9%85%D8%AE%D8%B5%D9%88%D8%B5/");
			// testUrl("http://ashpazi.blog.ir/sitemap.xml");
			// testUrl("http://ashpazi.blog.ir/1391/12/05/%D9%BE%DB%8C%D8%AA%D8%B2%D8%A7-%D9%85%D8%AE%D9%84%D9%88%D8%B7-%D9%85%D8%AE%D8%B5%D9%88%D8%B5");
			// testUrl("http://ashpazi.blog.ir/tag/غذای سنتی");
			// testUrl("http://ashpazi.blog.ir/");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void testUrl(String urlStr) throws Exception {
		InputStreamReader configReader = new InputStreamReader(UrlTester.class.getResourceAsStream("/extractors.xml"));
		ExtractorConfig extractorConfig = ExtractorConfig.readConfig(configReader);
		ExtractEngine engine = new ExtractEngine(extractorConfig);

		URL url = new URL(urlStr);
		System.out.println(url.toString());
		Protocol protocol = ProtocolFactory.getInstance().getProtocol(url);
		Content content = protocol.fetch(url, new HashMap<String, Object>());

		List<ExtractedDoc> res = engine.extract(content);
		for (ExtractedDoc doc : res) {
			System.out.println(doc);
		}
	}
}
