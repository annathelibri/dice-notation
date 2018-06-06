package pw.aru.dice.evaluator;

import pw.aru.dice.ast.Expr;
import pw.aru.dice.ast.Invocation;
import pw.aru.dice.ast.operations.BinaryOperation;
import pw.aru.dice.ast.operations.UnaryOperation;
import pw.aru.dice.ast.value.DiceNode;
import pw.aru.dice.ast.value.SolvedDiceNode;
import pw.aru.dice.pipelines.ExprModifier;

import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

/**
 * Solves ASTs. {@link DiceNode}s are transformed into {@link SolvedDiceNode}, which can then be used on the {@link DiceEvaluator}.
 *
 * @see DiceEvaluator
 */
public class DiceSolver extends ExprModifier {
    private final IntBinaryOperator random;

    public DiceSolver(IntBinaryOperator random) {
        this.random = random;
    }

    public DiceSolver(IntUnaryOperator random) {
        this.random = ((sides, i) -> random.applyAsInt(sides));
    }

    @Override
    public String toString() {
        return "DiceSolver@" + Integer.toHexString(hashCode());
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
        return new SolvedDiceNode(expr.getPosition(), expr.getRolls(), expr.getSides(), random);
    }
}
