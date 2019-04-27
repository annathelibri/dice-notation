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

package pw.aru.libs.dicenotation.parser.parslets;

import pw.aru.libs.dicenotation.ast.Expr;
import pw.aru.libs.dicenotation.lexer.Token;
import pw.aru.libs.dicenotation.lexer.TokenType;
import pw.aru.libs.dicenotation.parser.DiceParser;
import pw.aru.libs.dicenotation.parser.PrefixParser;

public class GroupParser implements PrefixParser {
    @Override
    public Expr parse(DiceParser parser, Token token) {
        Expr expr = parser.parseExpr();
        parser.eat(TokenType.RIGHT_PAREN);
        return expr;
    }
}
