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

public class ExprPipeline extends ExprModifier {
    static String p(ExprPipeline pipeline) {
        ExprVisitor<Expr> left = pipeline.left, right = pipeline.right;
        if (left instanceof ExprPipeline) {
            return p((ExprPipeline) left) + " > " + pipeline.right;
        } else {
            return left + " > " + pipeline.right;
        }
    }

    private final ExprVisitor<Expr> left;
    private final ExprVisitor<Expr> right;

    public ExprPipeline(ExprVisitor<Expr> left, ExprVisitor<Expr> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "ExprPipeline[" + p(this) + "]";
    }

    @Override
    public Expr visit(Identifier expr) {
        return expr.accept(left).accept(right);
    }

    @Override
    public Expr visit(Invocation expr) {
        return expr.accept(left).accept(right);
    }

    @Override
    public Expr visit(BinaryOperation expr) {
        return expr.accept(left).accept(right);
    }

    @Override
    public Expr visit(UnaryOperation expr) {
        return expr.accept(left).accept(right);
    }

    @Override
    public Expr visit(IntNode expr) {
        return expr.accept(left).accept(right);
    }

    @Override
    public Expr visit(DecimalNode expr) {
        return expr.accept(left).accept(right);
    }

    @Override
    public Expr visit(DiceNode expr) {
        return expr.accept(left).accept(right);
    }

    @Override
    public Expr visit(SolvedDiceNode expr) {
        return expr.accept(left).accept(right);
    }
}
