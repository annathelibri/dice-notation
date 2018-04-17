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

package jibril.dice.parser;

import jibril.dice.ast.Expr;
import jibril.dice.ast.Identifier;
import jibril.dice.ast.Invocation;
import jibril.dice.ast.operations.BinaryOperation;
import jibril.dice.ast.operations.BinaryOperatorType;
import jibril.dice.ast.operations.UnaryOperation;
import jibril.dice.ast.operations.UnaryOperatorType;
import jibril.dice.ast.value.DecimalNode;
import jibril.dice.ast.value.DiceNode;
import jibril.dice.ast.value.IntNode;

/**
 * Default precedence table for the Kaiper parser.
 */
public class Precedence {
    /* a + b | a - b */
    public static final int ADDITIVE = 8;
    /* a ^ b */
    public static final int EXPONENTIAL = 11;
    /* a * b | a / b | a % b */
    public static final int MULTIPLICATIVE = 9;
    /* a(b) */
    public static final int POSTFIX = 13;
    /* -a | +a | !a | ~a */
    public static final int PREFIX = 10;
    /* Int, Decimals, Dice Notation */
    public static final int PURE = 6;
    public static final int SHIFT = 7;

    public static int of(BinaryOperatorType op) {
        switch (op) {
            case PLUS:
            case MINUS:
                return Precedence.ADDITIVE;

            case TIMES:
            case DIVIDE:
            case MODULUS:
                return Precedence.MULTIPLICATIVE;

            case POWER:
                return Precedence.EXPONENTIAL;

            case SHL:
            case SHR:
                return Precedence.SHIFT;

            default:
                throw new RuntimeException("Impossible to happen.");
        }
    }

    public static int of(UnaryOperatorType op) {
        return Precedence.PREFIX;
    }

    public static int of(Expr expr) {
        if (expr instanceof DecimalNode) return Precedence.PURE;
        if (expr instanceof DiceNode) return Precedence.PURE;
        if (expr instanceof IntNode) return Precedence.PURE;
        if (expr instanceof Identifier) return Precedence.PURE;
        if (expr instanceof Invocation) return Precedence.POSTFIX;
        if (expr instanceof BinaryOperation) return Precedence.of(((BinaryOperation) expr).getOperator());
        if (expr instanceof UnaryOperation) return Precedence.of(((UnaryOperation) expr).getOperator());
        throw new RuntimeException("Impossible to happen.");
    }
}
