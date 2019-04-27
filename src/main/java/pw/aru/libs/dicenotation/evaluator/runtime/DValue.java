package pw.aru.libs.dicenotation.evaluator.runtime;

/**
 * A value that can be referred on the dice notation.
 */
public interface DValue {
    static DValue of(Number value) {
        return () -> value;
    }

    Number get();
}
