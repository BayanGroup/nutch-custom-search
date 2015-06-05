package ir.co.bayan.simorq.zal.extractor.process;

import ir.co.bayan.simorq.zal.extractor.evaluation.EvaluationContext;

import java.util.List;

/**
 * A template processor that processes each input item separately as a string.
 * @author Taha Ghasemi <taha.ghasemi@gmail.com>
 */
public abstract class StringProcessor implements Processor {
    @Override
    public List<Object> process(Object root, EvaluationContext context, List<Object> input) {
        for(int i = 0; i < input.size(); i++)
            input.set(i, process((String) input.get(i)));
        return input;
    }

    protected abstract String process(String item);
}
