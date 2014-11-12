package ir.co.bayan.simorq.zal.extractor.util;

import ir.co.bayan.simorq.zal.extractor.core.Content;
import ir.co.bayan.simorq.zal.extractor.core.ExtractEngine;
import ir.co.bayan.simorq.zal.extractor.core.ExtractedDoc;
import ir.co.bayan.simorq.zal.extractor.model.ExtractorConfig;
import ir.co.bayan.simorq.zal.extractor.protocol.Protocol;
import ir.co.bayan.simorq.zal.extractor.protocol.ProtocolFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
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
		// args = new String[] { "http://www.google.com", "src/test/resources/extractors.xml" };
		// args = new String[] { "http://gsas.harvard.edu/", "src/test/resources/teste.xml" };
		if (args.length != 2) {
			System.out.println("UrlTester <url> <location-of extractors.xml>");
			System.out.println("e.g. UrlTester http://www.google.com /conf/extractors.xml");
			return;
		}
		try {
			testUrl(args[0], args[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testUrl(String urlStr, String extracotrPath) throws Exception {
		File path = new File(extracotrPath);
		System.out.println("Read config form " + path.getAbsolutePath());
		ExtractorConfig extractorConfig = null;
		Reader configReader = null;
		try {
			configReader = new BufferedReader(new FileReader(path));
			extractorConfig = ExtractorConfig.readConfig(configReader);
		} finally {
			if (configReader != null)
				configReader.close();
		}
		ExtractEngine engine = new ExtractEngine(extractorConfig);

		URL url = new URL(urlStr);
		System.out.println("Extracting from " + url.toString());
		Protocol protocol = ProtocolFactory.getInstance().getProtocol(url);
		Content content = protocol.fetch(url, new HashMap<String, Object>());

		List<ExtractedDoc> res = engine.extract(content);
		for (ExtractedDoc doc : res) {
			System.out.println(doc);
		}
	}
}
