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

package jibril.dice.ast.value;

import jibril.dice.ast.Expr;
import jibril.dice.ast.ExprVisitor;
import jibril.dice.lexer.Position;

public class DiceNode extends Expr {
    protected final int rolls;
    protected final int sides;

    public DiceNode(Position position, int rolls, int sides) {
        super(position);
        this.rolls = rolls;
        this.sides = sides;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return rolls + "d" + sides;
    }

    public int getRolls() {
        return rolls;
    }

    public int getSides() {
        return sides;
    }
}
