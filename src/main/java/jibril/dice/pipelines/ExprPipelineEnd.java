package jibril.dice.pipelines;

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

