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

import pw.aru.libs.dicenotation.ast.Expr;
import pw.aru.libs.dicenotation.exceptions.SyntaxException;
import pw.aru.libs.dicenotation.lexer.DiceLexer;
import pw.aru.libs.dicenotation.lexer.Token;
import pw.aru.libs.dicenotation.lexer.TokenType;

public class DiceParser extends Parser {
    public static boolean OPTIMIZE_BINARY_OPERATIONS = false;

    public DiceParser(DiceLexer tokens) {
        super(tokens, DefaultGrammar.INSTANCE);
    }

    public DiceParser(DiceParser proxy) {
        super(proxy);
    }

    public Expr parse() {
        Expr expr = parseExpr();

        if (!getTokens().isEmpty()) {
            Token t = getTokens().get(0);
            if (t.getType() != TokenType.EOF) {
                throw new SyntaxException("Unexpected " + t, t.getPosition());
            }
        }

        return expr;
    }

    public Expr parseExpr() {
        return parseExpr(0);
    }

    public Expr parseExpr(int precedence) {
        Token token = eat();

        Expr expr = parsePrefix(token);

        return parseInfix(precedence, expr);
    }

    public Expr parseInfix(int precedence, Expr left) {
        while (precedence < getPrecedence()) {
            Token token = eat();

            InfixParser infix = getInfixParsers().get(token.getType());

            if (infix == null) {
                throw new SyntaxException("Unexpected " + token, token.getPosition());
            } else {
                left = infix.parse(this, left, token);
            }
        }
        return left;
    }

    public Expr parsePrefix(Token token) {
        PrefixParser prefix = getPrefixParsers().get(token.getType());

        if (prefix == null) throw new SyntaxException("Unexpected " + token, token.getPosition());

        return prefix.parse(this, token);
    }
}
