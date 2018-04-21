package snail.compiler.parser;

import javax.annotation.Nonnull;
import java.util.Collections;

public class Text {
    private final String text;

    public Text(@Nonnull String text) {
        this.text = text.replace("\r\n", "\n").replace("\r", "\n");
    }

    public Position at(int off) {
        if (off < 0 || off >= text.length())
            throw new IllegalArgumentException();
        final int line = Math.toIntExact(text.substring(0, 0).chars().filter(n -> n == '\n').count() + 1);
        final int column = line == 1 ? off + 1 :
                (text.length() - text.lastIndexOf('\n')) + 1;
        return new Position(off, line, column);
    }

    public Token tokenAt(int start, @Nonnull TokenType type, int length) {
        return new Token(this, start, length, type);
    }

    public String substring(int start, int length) {
        if (start < 0 || length < 0 || (start + length) >= text.length())
            throw new IllegalArgumentException();
        return text.substring(start, start + length);
    }

    public final class Position {
        private final int off, line, column;

        private Position(int off, int line, int column) {
            if (off < 0 || line < 1 || column < 1)
                throw new IllegalArgumentException();
            this.off = off;
            this.line = line;
            this.column = column;
        }

        public int off() {
            return off;
        }

        public int line() {
            return line;
        }

        public int column() {
            return column;
        }

        @Override
        public String toString() {
            return "(" + line() + "," + column() + ")";
        }

        public String where(@Nonnull String msg) {
            final StringBuilder b = new StringBuilder();
            b.append("|").append(toString()).append(": ").append(msg).append("\n");
            b.append("|").append(String.join("", Collections.nCopies(toString().length(), " ")));
            b.append(String.join("", Collections.nCopies(column, " ")));
            b.append("^");
            return b.toString();
        }
    }
}
