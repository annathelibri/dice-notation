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

package pw.aru.dice.ast.value;

import pw.aru.dice.ast.ExprVisitor;
import pw.aru.dice.lexer.Position;

import java.util.LinkedList;
import java.util.List;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

public class SolvedDiceNode extends DiceNode {
    private final List<Integer> dice;

    public SolvedDiceNode(Position position, int rolls, int sides, List<Integer> dice) {
        super(position, rolls, sides);
        this.dice = dice;
    }

    public SolvedDiceNode(Position position, int rolls, int sides, IntUnaryOperator random) {
        super(position, rolls, sides);
        this.dice = new LinkedList<>();
        for (int i = 0; i < rolls; i++) {
            dice.add(random.applyAsInt(sides));
        }
    }

    public SolvedDiceNode(Position position, int rolls, int sides, IntBinaryOperator random) {
        super(position, rolls, sides);
        this.dice = new LinkedList<>();
        for (int i = 0; i < rolls; i++) {
            dice.add(random.applyAsInt(sides, i));
        }
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return toString(dice) + super.toString();
    }

    public List<Integer> getDice() {
        return dice;
    }

    public int getResult() {
        return dice.stream().reduce(0, Integer::sum);
    }

    public String toString(List<Integer> list) {
        return list.stream()
            .map(it -> (it == 1 || it == sides) ? "**" + it + "**" : String.valueOf(it))
            .collect(Collectors.joining(", ", "[", "]"));
    }
}
