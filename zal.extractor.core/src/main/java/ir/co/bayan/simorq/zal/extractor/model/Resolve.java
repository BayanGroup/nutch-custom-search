package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext;
import org.apache.commons.lang3.Validate;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Resolves a possible relative url to absolute one based on the url in the context.
 * Some codes are copied from URLUtil of nutch.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
@XmlRootElement
public class Resolve extends Function {

	@SuppressWarnings("unchecked")
	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		Validate.isTrue(args != null && args.size() == 1, "Only one arg should be specified");

		List<String> res = (List<String>) args.get(0).extract(root, context);
		for (int i = 0; i < res.size(); i++) {
			String url = res.get(i);
			try {
				url = resolveURL(context.getContent().getUrl(), url).toString();
				res.set(i, url);
			} catch (MalformedURLException e) {
				// ignore
			}
		}
		return res;
	}

    /**
     * Resolve relative URL-s and fix a few java.net.URL errors
     * in handling of URLs with embedded params and pure query
     * targets.
     * @param base base url
     * @param target target url (may be relative)
     * @return resolved absolute url.
     * @throws MalformedURLException
     */
    public static URL resolveURL(URL base, String target)
            throws MalformedURLException {
        target = target.trim();

    /* this is probably not needed anymore - see NUTCH-797.
    // handle params that are embedded into the base url - move them to target
    // so URL class constructs the new url class properly
    if (base.toString().indexOf(';') > 0)
      return fixEmbeddedParams(base, target);
    */

        // handle the case that there is a target that is a pure query,
        // for example
        // http://careers3.accenture.com/Careers/ASPX/Search.aspx?co=0&sk=0
        // It has urls in the page of the form href="?co=0&sk=0&pg=1", and by
        // default
        // URL constructs the base+target combo as
        // http://careers3.accenture.com/Careers/ASPX/?co=0&sk=0&pg=1, incorrectly
        // dropping the Search.aspx target
        //
        // Browsers handle these just fine, they must have an exception similar to
        // this
        if (target.startsWith("?")) {
            return fixPureQueryTargets(base, target);
        }

        return new URL(base, target);
    }

    /** Handle the case in RFC3986 section 5.4.1 example 7, and similar. */
    static URL fixPureQueryTargets(URL base, String target)
            throws MalformedURLException {
        if (!target.startsWith("?")) return new URL(base, target);

        String basePath = base.getPath();
        String baseRightMost = "";
        int baseRightMostIdx = basePath.lastIndexOf("/");
        if (baseRightMostIdx != -1) {
            baseRightMost = basePath.substring(baseRightMostIdx + 1);
        }

        if (target.startsWith("?")) target = baseRightMost + target;

        return new URL(base, target);
    }

	@Override
	public String toString() {
		return "Resolve [" + super.toString() + "]";
	}

}
