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

package pw.aru.dice.lexer;

import pw.aru.dice.exceptions.SyntaxException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DiceLexer {
    private static final class Entry {
        private final char character;
        private final long index;
        private final long line;
        private final long lineIndex;

        private Entry(long index, long line, long lineIndex, char character) {
            this.index = index;
            this.line = line;
            this.lineIndex = lineIndex;
            this.character = character;
        }

        @Override
        public String toString() {
            return String.valueOf(character);
        }
    }

    private char current;
    private boolean eof;
    private Entry[] history;
    private long index;
    private long line;
    private long lineIndex;
    private int previous;
    private Reader reader;
    private List<Token> tokens;

    public DiceLexer(InputStream inputStream) {
        this(new InputStreamReader(inputStream));
    }

    public DiceLexer(String s) {
        this(new StringReader(s));
    }

    public DiceLexer(Reader reader) {
        this(reader, 2);
    }

    public DiceLexer(Reader reader, int historyBuffer) {
        this.reader = reader.markSupported() ? reader : new BufferedReader(reader);
        this.eof = false;
        this.tokens = new ArrayList<>();

        history = new Entry[historyBuffer];

        this.current = 0;
        this.index = -1;
        this.lineIndex = 0;
        this.line = 1;

        do {
            readTokens();
        } while (hasNext());

        if (lastToken().getType() != TokenType.EOF) {
            tokens.add(new Token(getPosition(), TokenType.EOF));
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make a printable string of this KaiperLexer.
     *
     * @return " at {index} [{character} : {line}]"
     */
    @Override
    public String toString() {
        return getPosition().toString();
    }

    /**
     * Get the readToken character in the source string.
     *
     * @return The readToken character, or 0 if past the end of the source string.
     */
    private char advance() {
        int c;
        if (this.previous != 0) {
            this.previous--;

            Entry entry = history[previous];

            current = entry.character;
            index = entry.index;
            line = entry.line;
            lineIndex = entry.lineIndex;

            return this.current;
        } else {
            try {
                c = this.reader.read();
            } catch (IOException exception) {
                throw new SyntaxException("Exception occurred while lexing", getPosition(), exception);
            }

            if (c <= 0) { // End of stream
                this.eof = true;
                c = 0;
            }
        }

        this.index += 1;
        if (this.current == '\r') {
            this.line += 1;
            this.lineIndex = c == '\n' ? 0 : 1;
        } else if (c == '\n') {
            this.line += 1;
            this.lineIndex = 0;
        } else {
            this.lineIndex += 1;
        }
        this.current = (char) c;

        System.arraycopy(history, 0, history, 1, history.length - 1);

        history[0] = new Entry(index, line, lineIndex, current);

        return this.current;
    }

    /**
     * Consume the next character, and check that
     * it matches a specified character.
     *
     * @param c The character to match.
     * @return The character.
     */
    private char advance(char c) {
        char n = this.advance();
        if (n != c) {
            throw new SyntaxException("Expected '" + c + "' and instead saw '" + n + "'", getPosition());
        }
        return n;
    }

    /**
     * Get the next n characters.
     *
     * @param n The number of characters to take.
     * @return A string of n characters.
     * @throws SyntaxException Substring bounds error if there are not
     *                         n characters remaining in the source string.
     */
    private String advance(int n) {
        if (n == 0) {
            return "";
        }

        char[] chars = new char[n];
        int pos = 0;

        while (pos < n) {
            chars[pos] = this.advance();
            if (!this.hasNext()) {
                throw new SyntaxException("Substring bounds error", getPosition());
            }
            pos += 1;
        }
        return new String(chars);
    }

    /**
     * Get the next char in the string, skipping whitespace.
     *
     * @return A character, or 0 if there are no more characters.
     */
    private char advanceClean() {
        while (true) {
            char c = this.advance();
            if (c == 0 || c > ' ') {
                return c;
            }
        }
    }

    /**
     * Get the text up but not including the specified character or the
     * end of line, whichever comes first.
     *
     * @param delimiter A delimiter character.
     * @return A string.
     */
    private String advanceTo(char delimiter) {
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = this.advance();
            if (c == delimiter || c == 0 || c == '\n' || c == '\r') {
                if (c != 0) {
                    this.back();
                }
                return sb.toString().trim();
            }
            sb.append(c);
        }
    }

    /**
     * Get the text up but not including one of the specified delimiter
     * characters or the end of line, whichever comes first.
     *
     * @param delimiters A set of delimiter characters.
     * @return A string, trimmed.
     */
    private String advanceTo(String delimiters) {
        char c;
        StringBuilder sb = new StringBuilder();
        while (true) {
            c = this.advance();
            if (delimiters.indexOf(c) >= 0 || c == 0 ||
                c == '\n' || c == '\r') {
                if (c != 0) {
                    this.back();
                }
                return sb.toString().trim();
            }
            sb.append(c);
        }
    }

    /**
     * Back up one character. This provides a sort of lookahead capability,
     * so that you can test for a digit or letter before attempting to parse
     * the readToken number or identifier.
     */
    private void back() {
        previous++;
    }

    /**
     * Return the current lexer's positional data.
     *
     * @return A plain object with position data.
     */
    public Position getPosition() {
        return new Position(index, line, lineIndex);
    }

    /**
     * Get the current list of tokens.
     *
     * @return
     */
    public List<Token> getTokens() {
        return tokens;
    }

    public boolean hasNext() {
        return !(previous == 0 && this.eof);
    }

    private Token lastToken() {
        return tokens.get(tokens.size() - 1);
    }

    private Token make(TokenType type) {
        return make(new Position(index, line, lineIndex), type);
    }

    private Token make(TokenType type, String value) {
        return make(new Position(index - value.length(), line, lineIndex - value.length()), type, value);
    }

    private Token make(Position position, TokenType type) {
        return new Token(position, type);
    }

    private Token make(Position position, TokenType type, String value) {
        return new Token(position, type, value);
    }

    /**
     * Peek and advance if the prompt is the same as the peeked character.
     *
     * @param prompt The character to match.
     * @return if the prompt is the same as the peeked character.
     */
    private boolean match(char prompt) {
        if (advance() == prompt) {
            return true;
        }
        back();
        return false;
    }

    /**
     * Get and remove a token from the tokens list.
     *
     * @return The next token.
     */
    public Token next() {
        return tokens.remove(0);
    }

    private void nextBlockComment() {
        while (hasNext()) {
            if (peek() == '*') {
                advance();
                if (peek() == '/') {
                    advance();
                    break;
                }
                back();
            }
            advance();
        }
    }

    /**
     * @return The next character.
     */
    private char peek() {
        char c = advance();
        back();
        return c;
    }

    private void push(Token token) {
        if (token == null) throw new IllegalArgumentException();
        tokens.add(token);
    }

    // useful for lexer-phase desugaring
    private void queue(char character) {
        System.arraycopy(history, previous + 1, history, 3, history.length - 3);
        history[previous] = new Entry(index, line, lineIndex, character);
        back();
    }

    private void readComment() {
        while (hasNext() && peek() != '\n') {
            advance();
        }
    }

    private void readDiceNotation(StringBuilder sb) {
        char c;

        while (true) {
            c = advance();

            if (Character.isDigit(c)) {
                sb.append(c);
            } else {
                back();
                push(make(TokenType.DICE_NOTATION, sb.toString()));
                return;
            }
        }
    }

    private void readName(char init) {
        readName(String.valueOf(init));
    }

    private void readName(String init) {
        StringBuilder sb = new StringBuilder(init);

        char c;
        while (true) {
            c = advance();
            if (!Character.isLetterOrDigit(c) && c != '_') break;
            sb.append(c);
        }

        back();

        String s = sb.toString();

        if (s.length() > 1 && s.charAt(0) == 'd' && Character.isDigit(s.charAt(1))) {
            push(make(TokenType.DICE_NOTATION, s));
        } else {
            push(make(TokenType.IDENTIFIER, s));
        }
    }

    private void readNumber(char init) {
        boolean point = false;

        char c;

        StringBuilder sb = new StringBuilder();
        sb.append(init);

        while (true) {
            c = advance();

            if (Character.isDigit(c)) {
                sb.append(c);
            } else switch (c) {
                case 'd':
                case 'D': {
                    if (point) throw new SyntaxException("`" + sb + "` is a number, but the dice notation requires an integer", getPosition());
                    readDiceNotation(sb.append('d'));
                    return;
                }
                case '.': {
                    if (point) {
                        back();
                        Token token = make(TokenType.NUMBER, sb.toString());
                        push(token);
                        return;
                    }

                    if (!Character.isDigit(peek())) {
                        back();
                        push(make(TokenType.INT, sb.toString()));
                        return;
                    }

                    sb.append(c);
                    point = true;
                    break;
                }
                default: {
                    back();
                    if (point) {
                        push(make(TokenType.NUMBER, sb.toString()));
                        return;
                    } else {
                        push(make(TokenType.INT, sb.toString()));
                        return;
                    }
                }
            }
        }
    }

    private void readTokens() {
        if (!hasNext()) {
            tokens.add(make(TokenType.EOF));
            return;
        }

        char c = advance();

        while (Character.isSpaceChar(c) || c == '\t') c = advance();

        switch (c) {
            case '(': {
                push(make(TokenType.LEFT_PAREN));
                return;
            }
            case ')': {
                if (lastToken().getType() == TokenType.LINE) {
                    tokens.remove(tokens.size() - 1);
                }

                push(make(TokenType.RIGHT_PAREN));
                return;
            }

            case '.': {
                push(make(TokenType.DOT));
                return;
            }
            case ',': {
                if (lastToken().getType() == TokenType.LINE) {
                    tokens.remove(tokens.size() - 1);
                }

                push(make(TokenType.COMMA));
                return;
            }
            case '+': {
                push(make(TokenType.PLUS));
                return;
            }
            case '-': {
                push(make(TokenType.MINUS));
                return;
            }
            case '*': {
                push(make(TokenType.ASTERISK));
                return;
            }
            case '/': {
                Token token;
                if (match('/')) {
                    readComment();
                    return;
                } else if (match('*')) {
                    nextBlockComment();
                    return;
                } else {
                    token = make(TokenType.SLASH);
                }
                push(token);
                return;
            }
            case '%': {
                push(make(TokenType.PERCENT));
                return;
            }
            case '^': {
                push(make(TokenType.CARET));
                return;
            }

            case '>': {
                if (!match('>')) throw new SyntaxException("Unrecognized `" + c + "`", getPosition());
                push(make(TokenType.SHIFT_RIGHT));
                return;
            }

            case '<': {
                if (!match('<')) throw new SyntaxException("Unrecognized `" + c + "`", getPosition());
                push(make(TokenType.SHIFT_LEFT));
                return;
            }

            case '\r':
            case '\n': {
                if (!tokens.isEmpty()) {
                    switch (lastToken().getType()) {
                        case LEFT_PAREN:
                        case COMMA:
                        case LINE:
                            break;
                        default:
                            push(make(TokenType.LINE));
                    }
                }
                return;
            }

            case '\0': {
                push(make(TokenType.EOF));
                return;
            }

            case (char) -1: {
                push(make(TokenType.EOF));
                return;
            }

            default: {
                if (Character.isDigit(c)) {
                    readNumber(c);
                    return;
                } else if (Character.isLetter(c)) {
                    readName(c);
                    return;
                }
                throw new SyntaxException("Unrecognized `" + c + "`", getPosition());
            }
        }
    }

    /**
     * Skip characters until the readToken character is the requested character.
     * If the requested character is not found, no characters are skipped.
     *
     * @param to A character to skip to.
     * @return The requested character, or zero if the requested character
     * is not found.
     */
    private char skipTo(char to) {
        char c;
        try {
            long startIndex = this.index;
            long startCharacter = this.lineIndex;
            long startLine = this.line;
            this.reader.mark(1000000);
            do {
                c = this.advance();
                if (c == 0) {
                    this.reader.reset();
                    this.index = startIndex;
                    this.lineIndex = startCharacter;
                    this.line = startLine;
                    return c;
                }
            } while (c != to);
        } catch (IOException exception) {
            throw new SyntaxException("Exception occurred while lexing", getPosition(), exception);
        }
        this.back();
        return c;
    }
}