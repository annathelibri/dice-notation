package jibril.dice.evaluator.runtime;

/**
 * A function that can be called on the dice notation.
 */
public interface DFunction {
    Number call(Number... args);
}
