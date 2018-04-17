package jibril.dice.evaluator;

import jibril.dice.ast.Expr;
import jibril.dice.ast.ExprVisitor;
import jibril.dice.ast.Identifier;
import jibril.dice.ast.Invocation;
import jibril.dice.ast.operations.BinaryOperation;
import jibril.dice.ast.operations.UnaryOperation;
import jibril.dice.ast.value.DecimalNode;
import jibril.dice.ast.value.DiceNode;
import jibril.dice.ast.value.IntNode;
import jibril.dice.ast.value.SolvedDiceNode;
import jibril.dice.evaluator.runtime.DFunction;
import jibril.dice.evaluator.runtime.DValue;
import jibril.dice.exceptions.EvaluationException;
import jibril.dice.utils.Numbers;

import java.util.Map;

import static java.util.Collections.emptyMap;

/**
 * Evaluates the math of a solved AST.
 *
 * @see ExprVisitor
 * @see DiceSolver
 * @see DiceEvaluatorBuilder
 */
public class DiceEvaluator implements ExprVisitor<Number> {
    private final Map<String, DFunction> functions;
    private final Map<String, DValue> values;

    public DiceEvaluator() {
        this(emptyMap(), emptyMap());
    }

    public DiceEvaluator(Map<String, DValue> values, Map<String, DFunction> functions) {
        this.values = values;
        this.functions = functions;
    }

    @Override
    public String toString() {
        return "DiceEvaluator[" +
            "values=" + values.keySet() +
            ", functions=" + functions.keySet() +
            ']';
    }

    @Override
    public Number visit(Identifier expr) {
        DValue v = values.get(expr.getName());
        if (v == null) throw new EvaluationException("Value `" + expr.getName() + "` doesn't exist.");
        return v.get();
    }

    @Override
    public Number visit(Invocation expr) {
        DFunction f = functions.get(expr.getName());
        if (f == null) throw new EvaluationException("Function `" + expr.getName() + "` doesn't exist.");
        Number[] args = expr.getArguments().stream().map(this::apply).toArray(Number[]::new);
        return f.call(args);
    }

    @Override
    public Number visit(BinaryOperation expr) {
        Number left = expr.getLeft().accept(this), right = expr.getRight().accept(this);

        switch (expr.getOperator()) {
            case PLUS:
                return Numbers.plus(left, right);
            case MINUS:
                return Numbers.minus(left, right);
            case TIMES:
                return Numbers.times(left, right);
            case DIVIDE:
                return Numbers.divide(left, right);
            case MODULUS:
                return Numbers.modulus(left, right);
            case POWER:
                return Numbers.power(left, right);
            case SHL:
                return Numbers.leftShift(left, right);
            case SHR:
                return Numbers.rightShift(left, right);
        }

        throw new EvaluationException("Impossible to happen.");
    }

    @Override
    public Number visit(UnaryOperation expr) {
        Number target = expr.getTarget().accept(this);

        switch (expr.getOperator()) {
            case PLUS:
                return Numbers.unaryPlus(target);
            case MINUS:
                return Numbers.unaryMinus(target);
        }

        throw new EvaluationException("Impossible to happen.");
    }

    @Override
    public Number visit(IntNode expr) {
        return expr.getValue();
    }

    @Override
    public Number visit(DecimalNode expr) {
        return expr.getValue();
    }

    @Override
    public Number visit(DiceNode expr) {
        throw new EvaluationException("Dice Notation wasn't previously solved. Use " + DiceSolver.class.getSimpleName() + " to solve Dice Expressions.");
    }

    @Override
    public Number visit(SolvedDiceNode expr) {
        return expr.getResult();
    }

    private Number apply(Expr expr) {
        return expr.accept(this);
    }
}
