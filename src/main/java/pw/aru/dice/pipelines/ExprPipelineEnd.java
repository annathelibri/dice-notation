package pw.aru.dice.pipelines;

import pw.aru.dice.ast.Expr;
import pw.aru.dice.ast.ExprVisitor;
import pw.aru.dice.ast.Identifier;
import pw.aru.dice.ast.Invocation;
import pw.aru.dice.ast.operations.BinaryOperation;
import pw.aru.dice.ast.operations.UnaryOperation;
import pw.aru.dice.ast.value.DecimalNode;
import pw.aru.dice.ast.value.DiceNode;
import pw.aru.dice.ast.value.IntNode;
import pw.aru.dice.ast.value.SolvedDiceNode;

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

