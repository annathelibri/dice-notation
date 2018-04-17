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

public class ExprModifier implements ExprVisitor<Expr> {
    public static ExprModifier of(ExprVisitor<Expr> exprVisitor) {
        return new VisitorModifier(exprVisitor);
    }

    @Override
    public Expr visit(Identifier expr) {
        return expr;
    }

    @Override
    public Expr visit(Invocation expr) {
        return expr;
    }

    @Override
    public Expr visit(BinaryOperation expr) {
        return expr;
    }

    @Override
    public Expr visit(UnaryOperation expr) {
        return expr;
    }

    @Override
    public Expr visit(IntNode expr) {
        return expr;
    }

    @Override
    public Expr visit(DecimalNode expr) {
        return expr;
    }

    @Override
    public Expr visit(DiceNode expr) {
        return expr;
    }

    @Override
    public Expr visit(SolvedDiceNode expr) {
        return expr;
    }

    public <R> ExprVisitor<R> andFinally(ExprVisitor<R> visitor) {
        return new ExprPipelineEnd<>(this, visitor);
    }

    public ExprModifier andThen(ExprVisitor<Expr> modifier) {
        return new ExprPipeline(this, modifier);
    }

    protected Expr apply(Expr expr) {
        return expr.accept(this);
    }
}

class VisitorModifier extends ExprModifier {
    private final ExprVisitor<Expr> raw;

    VisitorModifier(ExprVisitor<Expr> raw) {

        this.raw = raw;
    }

    @Override
    public Expr visit(Identifier expr) {
        return raw.visit(expr);
    }

    @Override
    public Expr visit(Invocation expr) {
        return raw.visit(expr);
    }

    @Override
    public Expr visit(BinaryOperation expr) {
        return raw.visit(expr);
    }

    @Override
    public Expr visit(UnaryOperation expr) {
        return raw.visit(expr);
    }

    @Override
    public Expr visit(IntNode expr) {
        return raw.visit(expr);
    }

    @Override
    public Expr visit(DecimalNode expr) {
        return raw.visit(expr);
    }

    @Override
    public Expr visit(DiceNode expr) {
        return raw.visit(expr);
    }

    @Override
    public Expr visit(SolvedDiceNode expr) {
        return raw.visit(expr);
    }

}