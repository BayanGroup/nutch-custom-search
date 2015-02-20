package ir.co.bayan.simorq.zal.extractor.model;

import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext;

import java.util.List;

/**
 * An element that takes part in the extraction process.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public interface Extractor {

	/**
	 * Extracts related data from the part of document represented by root.
	 */
	List<?> extract(Object root, EvaluationContext context) throws Exception;

}
