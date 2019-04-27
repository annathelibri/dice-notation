package pw.aru.libs.dicenotation.evaluator;

import pw.aru.libs.dicenotation.evaluator.runtime.DFunction;
import pw.aru.libs.dicenotation.evaluator.runtime.DValue;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builds {@link DiceEvaluator}s
 */
public class DiceEvaluatorBuilder {
    private final Map<String, DValue> constants = new LinkedHashMap<>();
    private final Map<String, DFunction> functions = new LinkedHashMap<>();

    public DiceEvaluator build() {
        return new DiceEvaluator(constants, functions);
    }

    public DiceEvaluatorBuilder function(String name, DFunction function) {
        functions.put(name, function);
        return this;
    }

    public DiceEvaluatorBuilder functionAlias(String name, String... alias) {
        DFunction f = functions.get(name);
        for (String k : alias) functions.put(k, f);
        return this;
    }

    public DiceEvaluatorBuilder value(String name, Number value) {
        constants.put(name, DValue.of(value));
        return this;
    }

    public DiceEvaluatorBuilder value(String name, DValue value) {
        constants.put(name, value);
        return this;
    }

    public DiceEvaluatorBuilder valueAlias(String name, String... alias) {
        DValue v = constants.get(name);
        for (String k : alias) constants.put(k, v);
        return this;
    }
}
