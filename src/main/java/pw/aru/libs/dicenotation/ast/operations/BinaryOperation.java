/*
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package pw.aru.libs.dicenotation.ast.operations;

import pw.aru.libs.dicenotation.ast.Expr;
import pw.aru.libs.dicenotation.ast.ExprVisitor;
import pw.aru.libs.dicenotation.lexer.Position;
import pw.aru.libs.dicenotation.parser.Precedence;

public class BinaryOperation extends Expr {
    private final Expr left;
    private final BinaryOperatorType operator;
    private final Expr right;

    public BinaryOperation(Position position, Expr left, Expr right, BinaryOperatorType operator) {
        super(position);
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void ast(StringBuilder builder, String indent, boolean isTail) {
        builder.append(indent).append(isTail ? "└── " : "├── ").append("binary ").append(operator);

        builder.append('\n');
        left.ast(builder, indent + (isTail ? "    " : "│   "), false);

        builder.append('\n');
        right.ast(builder, indent + (isTail ? "    " : "│   "), true);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int prec = Precedence.of(operator);
        int leftPrec = Precedence.of(left);
        int rightPrec = Precedence.of(right);

        if (leftPrec > prec) {
            sb.append('(').append(left).append(')');
        } else {
            sb.append(left);
        }

        sb.append(' ').append(operator.getOperator()).append(' ');

        if (rightPrec > prec) {
            sb.append('(').append(right).append(')');
        } else {
            sb.append(right);
        }

        return sb.toString();
    }

    public Expr getLeft() {
        return left;
    }

    public BinaryOperatorType getOperator() {
        return operator;
    }

    public Expr getRight() {
        return right;
    }

}
