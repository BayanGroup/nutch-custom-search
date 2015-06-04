package ir.co.bayan.simorq.zal.extractor.process;

import java.util.List;

/**
 * Processes a given input object an produces a result. Processors can be used in process function to post-process
 * result of other functions.
 * 
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 * 
 */
public interface Processor {

	List<Object> process(List<Object> input);

}
