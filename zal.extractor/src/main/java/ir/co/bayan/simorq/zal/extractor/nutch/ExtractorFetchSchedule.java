package ir.co.bayan.simorq.zal.extractor.nutch;

import ir.co.bayan.simorq.zal.extractor.core.ExtractEngine;
import ir.co.bayan.simorq.zal.extractor.model.Document;
import ir.co.bayan.simorq.zal.extractor.model.ExtractorConfig;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.nutch.crawl.AdaptiveFetchSchedule;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.metadata.Nutch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A fetch schedule that determines revisit schedules based on the values provided in extractors.xml file per document
 * using every/adaptive attributes.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractorFetchSchedule extends AdaptiveFetchSchedule {

	public static final Text WRITABLE_SET_INTERVAL = new Text("setExtractorInterval");

	private static final Logger LOGGER = LoggerFactory.getLogger(ExtractorFetchSchedule.class);

	@Override
	public void setConf(Configuration conf) {
		super.setConf(conf);
		try {
			ExtractEngine.getInstance().setConf(ExtractorConfig.readConfig(conf));
		} catch (Exception e) {
			LOGGER.error("Exception occured", e);
		}
	}

	@Override
	public CrawlDatum setFetchSchedule(Text url, CrawlDatum datum, long prevFetchTime, long prevModifiedTime,
			long fetchTime, long modifiedTime, int state) {
		// Check whether it previously being set
		if (!datum.getMetaData().containsKey(WRITABLE_SET_INTERVAL)) {
			Document doc = ExtractEngine.getInstance().findMatchingDoc(url.toString(), null);
			if (doc != null && doc.getEveryInMiliSecond() != null) {
				datum.setFetchInterval(doc.getEveryInMiliSecond());
				if (!doc.isAdaptive())
					datum.getMetaData().put(Nutch.WRITABLE_FIXED_INTERVAL_KEY,
							new FloatWritable(doc.getEveryInMiliSecond()));
			}
			// set the flag
			datum.getMetaData().put(WRITABLE_SET_INTERVAL, new BooleanWritable(true));
		}
		return super.setFetchSchedule(url, datum, prevFetchTime, prevModifiedTime, fetchTime, modifiedTime, state);
	}

}
