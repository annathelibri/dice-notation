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

package pw.aru.libs.dicenotation.parser;

import pw.aru.libs.dicenotation.ast.operations.BinaryOperatorType;
import pw.aru.libs.dicenotation.ast.operations.UnaryOperatorType;
import pw.aru.libs.dicenotation.lexer.TokenType;
import pw.aru.libs.dicenotation.parser.parslets.GroupParser;
import pw.aru.libs.dicenotation.parser.parslets.NameParser;
import pw.aru.libs.dicenotation.parser.parslets.nodes.DecimalParser;
import pw.aru.libs.dicenotation.parser.parslets.nodes.DiceNotationParser;
import pw.aru.libs.dicenotation.parser.parslets.nodes.IntParser;
import pw.aru.libs.dicenotation.parser.parslets.operators.BinaryOperatorParser;
import pw.aru.libs.dicenotation.parser.parslets.operators.UnaryOperatorParser;

public class DefaultGrammar extends Grammar {
    public static final Grammar INSTANCE = new DefaultGrammar();

    private DefaultGrammar() {
        // BLOCKS
        prefix(TokenType.LEFT_PAREN, new GroupParser());

        // NODES
        prefix(TokenType.INT, new IntParser());
        prefix(TokenType.NUMBER, new DecimalParser());
        prefix(TokenType.DICE_NOTATION, new DiceNotationParser());

        prefix(TokenType.IDENTIFIER, new NameParser());

        // Numeric
        prefix(TokenType.MINUS, new UnaryOperatorParser(UnaryOperatorType.MINUS));
        prefix(TokenType.PLUS, new UnaryOperatorParser(UnaryOperatorType.PLUS));
        infix(TokenType.PLUS, new BinaryOperatorParser(Precedence.ADDITIVE, true, BinaryOperatorType.PLUS));
        infix(TokenType.MINUS, new BinaryOperatorParser(Precedence.ADDITIVE, true, BinaryOperatorType.MINUS));
        infix(TokenType.ASTERISK, new BinaryOperatorParser(Precedence.MULTIPLICATIVE, true, BinaryOperatorType.TIMES));
        infix(TokenType.SLASH, new BinaryOperatorParser(Precedence.MULTIPLICATIVE, true, BinaryOperatorType.DIVIDE));
        infix(TokenType.PERCENT, new BinaryOperatorParser(Precedence.MULTIPLICATIVE, true, BinaryOperatorType.MODULUS));
        infix(TokenType.CARET, new BinaryOperatorParser(Precedence.EXPONENTIAL, false, BinaryOperatorType.POWER));
        infix(TokenType.SHIFT_RIGHT, new BinaryOperatorParser(Precedence.SHIFT, true, BinaryOperatorType.SHR));
        infix(TokenType.SHIFT_LEFT, new BinaryOperatorParser(Precedence.SHIFT, true, BinaryOperatorType.SHL));
    }
}
