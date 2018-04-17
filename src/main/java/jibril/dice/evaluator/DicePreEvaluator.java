package jibril.dice.evaluator;

import jibril.dice.ast.Expr;
import jibril.dice.ast.Invocation;
import jibril.dice.ast.operations.BinaryOperation;
import jibril.dice.ast.operations.UnaryOperation;
import jibril.dice.ast.value.DiceNode;
import jibril.dice.ast.value.IntNode;
import jibril.dice.ast.value.SolvedDiceNode;
import jibril.dice.pipelines.ExprModifier;

import java.util.stream.Collectors;

/**
 * Transforms solved ASTs into clean ones, transforming {@link SolvedDiceNode}s into {@link IntNode}s.
 *
 * @see DiceEvaluator
 * @see DiceSolver
 */
public class DicePreEvaluator extends ExprModifier {
    public static DicePreEvaluator INSTANCE = new DicePreEvaluator();

    private DicePreEvaluator() {
    }

    @Override
    public String toString() {
        return "DicePreEvaluator";
    }

    @Override
    public Expr visit(Invocation expr) {
        return new Invocation(expr.getPosition(), expr.getName(), expr.getArguments().stream().map(this::apply).collect(Collectors.toList()));
    }

    @Override
    public Expr visit(BinaryOperation expr) {
        return new BinaryOperation(expr.getPosition(), expr.getLeft().accept(this), expr.getRight().accept(this), expr.getOperator());
    }

    @Override
    public Expr visit(UnaryOperation expr) {
        return new UnaryOperation(expr.getPosition(), expr.getTarget().accept(this), expr.getOperator());
    }

    @Override
    public Expr visit(DiceNode expr) {
        throw new RuntimeException("Dice Notation wasn't previously solved.");
    }

    @Override
    public Expr visit(SolvedDiceNode expr) {
        return new IntNode(expr.getPosition(), expr.getResult());
    }
}
