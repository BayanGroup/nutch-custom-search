package ir.co.bayan.simorq.zal.extractor.nutch;

import org.apache.hadoop.io.Text;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.Inlinks;
import org.apache.nutch.indexer.NutchDocument;
import org.apache.nutch.indexer.NutchField;
import org.apache.nutch.parse.Parse;
import org.apache.nutch.scoring.ScoringFilterException;

/**
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class ExtractorScoringFilter extends OPICScoringFilter {

	@Override
	public float indexerScore(Text url, NutchDocument doc, CrawlDatum dbDatum, CrawlDatum fetchDatum, Parse parse,
			Inlinks inlinks, float initScore) throws ScoringFilterException {
		NutchField field = doc.getField("boost");
		if (field != null) {
			Object boost = field.getValues().get(0);
			doc.removeField("boost");
			return (Float) boost;
		}
		return super.indexerScore(url, doc, dbDatum, fetchDatum, parse, inlinks, initScore);
	}

}
