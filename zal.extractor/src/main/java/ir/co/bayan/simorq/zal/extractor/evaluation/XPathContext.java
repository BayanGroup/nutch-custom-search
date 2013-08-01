package ir.co.bayan.simorq.zal.extractor.evaluation;

import ir.co.bayan.simorq.zal.extractor.core.Content;

import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Element;

/**
 * Context used by {@link XPathEvaluator}.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public class XPathContext extends EvaluationContext {

	private final NamespaceContext nsContext;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public XPathContext(Evaluator<XPathContext> engine, Content content, Element root, NamespaceContext nsContext) {
		super((Evaluator) engine, content, root);
		this.nsContext = nsContext;
	}

	/**
	 * @return the root
	 */
	@Override
	public Element getMainRoot() {
		return (Element) mainRoot;
	}

	/**
	 * @return the nsContext
	 */
	public NamespaceContext getNsContext() {
		return nsContext;
	}

}
