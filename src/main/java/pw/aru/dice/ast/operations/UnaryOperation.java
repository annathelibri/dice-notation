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

package pw.aru.dice.ast.operations;

import pw.aru.dice.ast.Expr;
import pw.aru.dice.ast.ExprVisitor;
import pw.aru.dice.lexer.Position;
import pw.aru.dice.parser.Precedence;

public class UnaryOperation extends Expr {
    private final UnaryOperatorType operator;
    private final Expr target;

    public UnaryOperation(Position position, Expr target, UnaryOperatorType operator) {
        super(position);
        this.target = target;
        this.operator = operator;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void ast(StringBuilder builder, String indent, boolean isTail) {
        builder.append(indent).append(isTail ? "└── " : "├── ").append("unary ").append(operator);

        builder.append('\n');
        target.ast(builder, indent + (isTail ? "    " : "│   "), true);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int prec = Precedence.of(operator);
        int targetPrec = Precedence.of(target);

        sb.append(operator.getOperator());

        if (targetPrec > prec) {
            sb.append('(').append(target).append(')');
        } else {
            sb.append(target);
        }

        return sb.toString();
    }

    public UnaryOperatorType getOperator() {
        return operator;
    }

    public Expr getTarget() {
        return target;
    }
}
