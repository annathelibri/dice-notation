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

package pw.aru.libs.dicenotation.parser.parslets.nodes;

import pw.aru.libs.dicenotation.ast.Expr;
import pw.aru.libs.dicenotation.ast.value.DiceNode;
import pw.aru.libs.dicenotation.exceptions.SyntaxException;
import pw.aru.libs.dicenotation.lexer.Token;
import pw.aru.libs.dicenotation.parser.DiceParser;
import pw.aru.libs.dicenotation.parser.PrefixParser;

import static java.lang.Integer.parseInt;

public class DiceNotationParser implements PrefixParser {
    @Override
    public Expr parse(DiceParser parser, Token token) {
        String s = token.getString().toLowerCase();

        if (s.charAt(0) == 'd') {
            return new DiceNode(token.getPosition(), 1, parseInt(s.substring(1)));
        }

        int i = s.indexOf('d');

        if (i == -1) {
            throw new SyntaxException("Invalid dice notation `" + s + "`");
        }

        String rolls = s.substring(0, i), sides = s.substring(i + 1);

        return new DiceNode(token.getPosition(), parseInt(rolls), parseInt(sides));
    }
}
