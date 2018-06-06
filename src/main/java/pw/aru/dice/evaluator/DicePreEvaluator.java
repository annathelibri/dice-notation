package pw.aru.dice.evaluator;

import pw.aru.dice.ast.Expr;
import pw.aru.dice.ast.Invocation;
import pw.aru.dice.ast.operations.BinaryOperation;
import pw.aru.dice.ast.operations.UnaryOperation;
import pw.aru.dice.ast.value.DiceNode;
import pw.aru.dice.ast.value.IntNode;
import pw.aru.dice.ast.value.SolvedDiceNode;
import pw.aru.dice.pipelines.ExprModifier;

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
