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
import pw.aru.libs.dicenotation.ast.operations.BinaryOperation;
import pw.aru.libs.dicenotation.ast.operations.BinaryOperatorType;
import pw.aru.libs.dicenotation.ast.value.DecimalNode;
import pw.aru.libs.dicenotation.ast.value.IntNode;
import pw.aru.libs.dicenotation.exceptions.SyntaxException;
import pw.aru.libs.dicenotation.lexer.Token;
import pw.aru.libs.dicenotation.parser.BinaryParser;
import pw.aru.libs.dicenotation.parser.DiceParser;

public class BinaryOperatorParser extends BinaryParser {
    private final BinaryOperatorType operator;

    public BinaryOperatorParser(int precedence, boolean leftAssoc, BinaryOperatorType operator) {
        super(precedence, leftAssoc);
        this.operator = operator;
    }

    @Override
    public Expr parse(DiceParser parser, Expr left, Token token) {
        Expr right = parser.parseExpr(getPrecedence() - (isLeftAssoc() ? 0 : 1));

        if (DiceParser.OPTIMIZE_BINARY_OPERATIONS
            && (left instanceof IntNode || left instanceof DecimalNode)
            && (right instanceof IntNode || right instanceof DecimalNode)) {
            return optimizeArithmetic(parser, token, left, right, operator);
        }

        return new BinaryOperation(token.getPosition(), left, right, operator);
    }

    private Expr optimizeArithmetic(DiceParser parser, Token token, Expr left, Expr right, BinaryOperatorType operator) {
        boolean endInt = left instanceof IntNode && right instanceof IntNode;
        double leftValue;
        if (left instanceof IntNode) {
            leftValue = ((IntNode) left).getValue();
        } else {
            leftValue = ((DecimalNode) left).getValue();
        }

        double rightValue;
        if (right instanceof IntNode) {
            rightValue = ((IntNode) right).getValue();
        } else {
            rightValue = ((DecimalNode) right).getValue();
        }

        double finalValue;

        switch (operator) {
            case PLUS:
                finalValue = leftValue + rightValue;
                break;
            case MINUS:
                finalValue = leftValue - rightValue;
                break;
            case TIMES:
                finalValue = leftValue * rightValue;
                break;
            case DIVIDE:
                if (endInt && rightValue == 0) {
                    throw new SyntaxException("Division by 0", parser.getLast().getPosition());
                }
                finalValue = leftValue / rightValue;
                break;
            case MODULUS:
                finalValue = leftValue % rightValue;
                break;
            case POWER:
                finalValue = Math.pow(leftValue, rightValue);
                break;

            default:
                return new BinaryOperation(token.getPosition(), left, right, operator);
        }

        if (endInt) {
            return new IntNode(token.getPosition(), (int) finalValue);
        }

        return new DecimalNode(token.getPosition(), finalValue);
    }
}