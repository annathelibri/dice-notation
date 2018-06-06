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

package pw.aru.dice.parser;

import pw.aru.dice.ast.Expr;
import pw.aru.dice.lexer.Token;

public abstract class BinaryParser implements InfixParser {
    private final boolean leftAssoc;
    private final int precedence;

    public BinaryParser(int precedence) {
        this(precedence, true);
    }

    public BinaryParser(int precedence, boolean leftAssoc) {
        this.precedence = precedence;
        this.leftAssoc = leftAssoc;
    }

    public abstract Expr parse(DiceParser parser, Expr left, Token token);

    @Override
    public int getPrecedence() {
        return precedence;
    }

    public boolean isLeftAssoc() {
        return leftAssoc;
    }
}