package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.core.Content;
import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext;
import ir.co.bayan.simorq.zal.extractor.evaluation.Evaluator;
import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluatorFactory;
import ir.co.bayan.simorq.zal.extractor.protocol.Protocol;
import ir.co.bayan.simorq.zal.extractor.protocol.ProtocolFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Fetches a content from the given url and evaluates it with the specified engine. If checkModified is set (default),
 * the evaluation is only done if the resource is changed since the last fetch time.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * @see Protocol
 * @see Evaluator
 * 
 */
@XmlRootElement
public class Fetch extends Function {

	private List<FetchParameter> parameters;
	private Map<String, Object> paramObjects = new HashMap<String, Object>();

	private String url;

	private URL targetURL;
	private Protocol protocol;

	private String engine;
	private Evaluator<? extends EvaluationContext> evaluator;

	@XmlAttribute
	private boolean checkModified = true;

	private Map<String, Long> lastModificationPerUrl = new HashMap<>();

	/**
	 * @param url
	 *            the url to set
	 */
	@XmlAttribute(required = true)
	public void setUrl(String url) {
		this.url = url;
		try {
			targetURL = new URL(url);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Url is not valid " + url);
		}
		protocol = ProtocolFactory.getInstance().getProtocol(targetURL);
	}

	/**
	 * @return the parameters
	 */
	public List<FetchParameter> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	@XmlElementWrapper(name = "parameters")
	@XmlElement(name = "parameter")
	public void setParameters(List<FetchParameter> parameters) {
		this.parameters = parameters;
		for (FetchParameter fetchParameter : parameters)
			paramObjects.put(fetchParameter.getName(), fetchParameter.getValue());
	}

	/**
	 * @return the checkModified
	 */
	public boolean isCheckModified() {
		return checkModified;
	}

	/**
	 * @param checkModified
	 *            the checkModified to set
	 */
	public void setCheckModified(boolean checkModified) {
		this.checkModified = checkModified;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the engine
	 */
	public String getEngine() {
		return engine;
	}

	/**
	 * @return the protocol
	 */
	public Protocol getProtocol() {
		return protocol;
	}

	/**
	 * Note that this value will be overridden upon setting the url
	 * 
	 * @param protocol
	 *            the protocol to set
	 */
	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the evaluator
	 */
	public Evaluator<? extends EvaluationContext> getEvaluator() {
		return evaluator;
	}

	/**
	 * Note this value will be overridden upon setting engine property
	 * 
	 * @param evaluator
	 *            the evaluator to set
	 */
	public void setEvaluator(Evaluator<? extends EvaluationContext> evaluator) {
		this.evaluator = evaluator;
	}

	/**
	 * @param engine
	 *            the engine to set
	 */
	@XmlAttribute(required = true)
	public void setEngine(String engine) {
		this.engine = engine;
		evaluator = EvaluatorFactory.getInstance().getEvaluator(engine);
	}

	@Override
	public List<?> extract(Object root, EvaluationContext context) throws Exception {
		List<Object> res = new ArrayList<>();
		if (checkModified) {
			paramObjects.put(Protocol.PARAM_LAST_MODIFIED, lastModificationPerUrl.get(url));
		}
		Content content = protocol.fetch(targetURL, paramObjects);
		EvaluationContext newContext = evaluator.createContext(content);

		for (Function arg : args) {
			res.addAll(arg.extract(newContext.getRoot(), newContext));
		}

		if (checkModified) {
			synchronized (lastModificationPerUrl) {
				lastModificationPerUrl.put(url, System.currentTimeMillis());
			}
		}

		return res;
	}

	@Override
	public String toString() {
		return "Fetch [url=" + url + ", engine=" + engine + "]";
	}

}
