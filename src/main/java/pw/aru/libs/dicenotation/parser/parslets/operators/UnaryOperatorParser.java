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

package pw.aru.libs.dicenotation.parser.parslets.operators;

import pw.aru.libs.dicenotation.ast.Expr;
import pw.aru.libs.dicenotation.ast.operations.UnaryOperation;
import pw.aru.libs.dicenotation.ast.operations.UnaryOperatorType;
import pw.aru.libs.dicenotation.ast.value.DecimalNode;
import pw.aru.libs.dicenotation.ast.value.IntNode;
import pw.aru.libs.dicenotation.lexer.Token;
import pw.aru.libs.dicenotation.parser.DiceParser;
import pw.aru.libs.dicenotation.parser.Precedence;
import pw.aru.libs.dicenotation.parser.PrefixParser;

public class UnaryOperatorParser implements PrefixParser {
    private final UnaryOperatorType operator;

    public UnaryOperatorParser(UnaryOperatorType operator) {
        this.operator = operator;
    }

    @Override
    public Expr parse(DiceParser parser, Token token) {
        Expr left = parser.parseExpr(Precedence.PREFIX);

        if (parser.optimizeUnaryOperations && (left instanceof IntNode || left instanceof DecimalNode)) {
            return optimizeArithmetic(token, left, operator);
        }

        return new UnaryOperation(token.getPosition(), left, operator);
    }

    private Expr optimizeArithmetic(Token token, Expr left, UnaryOperatorType operator) {
        switch (operator) {
            case PLUS: {
                if (left instanceof IntNode) {
                    return new IntNode(token.getPosition(), ((IntNode) left).getValue());
                } else {
                    return new DecimalNode(token.getPosition(), ((DecimalNode) left).getValue());
                }
            }
            case MINUS: {
                if (left instanceof IntNode) {
                    return new IntNode(token.getPosition(), -((IntNode) left).getValue());
                } else {
                    return new DecimalNode(token.getPosition(), -((DecimalNode) left).getValue());
                }
            }

            default: {
                return new UnaryOperation(token.getPosition(), left, operator);
            }
        }
    }
}
