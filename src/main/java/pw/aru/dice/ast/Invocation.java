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

package pw.aru.dice.ast;

import pw.aru.dice.lexer.Position;

import java.util.List;
import java.util.stream.Collectors;

public class Invocation extends Expr {
    private final List<Expr> arguments;
    private final String name;

    public Invocation(Position position, String name, List<Expr> arguments) {
        super(position);
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void ast(StringBuilder builder, String indent, boolean isTail) {
        builder.append(indent).append(isTail ? "└── " : "├── ").append("invoke ").append(name);

        builder.append('\n');
        if (arguments.isEmpty()) {
            builder.append(indent).append(isTail ? "    " : "│   ").append("└── ").append("*");
        } else {
            for (int i = 0; i < arguments.size() - 1; i++) {
                arguments.get(i).ast(builder, indent + (isTail ? "    " : "│   "), false);
                builder.append('\n');
            }
            if (arguments.size() > 0) {
                arguments.get(arguments.size() - 1).ast(builder, indent + (isTail ? "    " : "│   "), true);
            }
        }
    }

    @Override
    public String toString() {
        return arguments.stream().map(Expr::toString).collect(Collectors.joining(", ", name + "(", ")"));
    }

    public List<Expr> getArguments() {
        return arguments;
    }

    public String getName() {
        return name;
    }
}
