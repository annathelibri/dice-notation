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

package pw.aru.dice.parser.parslets;

import pw.aru.dice.ast.Expr;
import pw.aru.dice.ast.Identifier;
import pw.aru.dice.ast.Invocation;
import pw.aru.dice.lexer.Token;
import pw.aru.dice.lexer.TokenType;
import pw.aru.dice.parser.DiceParser;
import pw.aru.dice.parser.PrefixParser;

import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.singletonList;

public class NameParser implements PrefixParser {
    @Override
    public Expr parse(DiceParser parser, Token token) {
        if (parser.nextIsAny(TokenType.INT, TokenType.NUMBER, TokenType.DICE_NOTATION, TokenType.IDENTIFIER)) {
            return new Invocation(
                token.getPosition(),
                token.getString(),
                singletonList(parser.parseExpr())
            );
        } else if (parser.nextIs(TokenType.LEFT_PAREN)) {
            List<Expr> args = new LinkedList<>();

            parser.eat(TokenType.LEFT_PAREN);

            if (!parser.nextIs(TokenType.RIGHT_PAREN)) while (true) {
                args.add(parser.parseExpr());

                if (parser.nextIs(TokenType.RIGHT_PAREN)) break;
                parser.eat(TokenType.COMMA);
            }

            parser.eat(TokenType.RIGHT_PAREN);

            return new Invocation(token.getPosition(), token.getString(), args);

        } else {
            return new Identifier(token.getPosition(), token.getString());
        }
    }
}
