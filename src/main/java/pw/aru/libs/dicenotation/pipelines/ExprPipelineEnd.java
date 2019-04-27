package pw.aru.libs.dicenotation.pipelines;

import pw.aru.libs.dicenotation.ast.Expr;
import pw.aru.libs.dicenotation.ast.ExprVisitor;
import pw.aru.libs.dicenotation.ast.Identifier;
import pw.aru.libs.dicenotation.ast.Invocation;
import pw.aru.libs.dicenotation.ast.operations.BinaryOperation;
import pw.aru.libs.dicenotation.ast.operations.UnaryOperation;
import pw.aru.libs.dicenotation.ast.value.DecimalNode;
import pw.aru.libs.dicenotation.ast.value.DiceNode;
import pw.aru.libs.dicenotation.ast.value.IntNode;
import pw.aru.libs.dicenotation.ast.value.SolvedDiceNode;

public class ExprPipelineEnd<T> implements ExprVisitor<T> {
    private final ExprVisitor<Expr> left;
    private final ExprVisitor<T> right;

    public ExprPipelineEnd(ExprVisitor<Expr> left, ExprVisitor<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "ExprPipeline[" + (left instanceof ExprPipeline ? ExprPipeline.p((ExprPipeline) left) : left) + " > " + right + "]";
    }

    @Override
    public T visit(Identifier expr) {
        return expr.accept(left).accept(right);
    }

    @Override
    public T visit(Invocation expr) {
        return expr.accept(left).accept(right);
    }

    @Override
    public T visit(BinaryOperation expr) {
        return expr.accept(left).accept(right);
    }

    @Override
    public T visit(UnaryOperation expr) {
        return expr.accept(left).accept(right);
    }

    @Override
    public T visit(IntNode expr) {
        return expr.accept(left).accept(right);
    }

    @Override
    public T visit(DecimalNode expr) {
        return expr.accept(left).accept(right);
    }

    @Override
    public T visit(DiceNode expr) {
        return expr.accept(left).accept(right);
    }

    @Override
    public T visit(SolvedDiceNode expr) {
        return expr.accept(left).accept(right);
    }

}

