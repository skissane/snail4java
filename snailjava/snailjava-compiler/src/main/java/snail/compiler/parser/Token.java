package snail.compiler.parser;

import javax.annotation.Nonnull;
import java.util.Objects;

public class Token {
    private final Text text;
    private final Text.Position start, end;
    private final TokenType type;
    private final String value;

    public Token(@Nonnull Text text, int start, int length, @Nonnull TokenType type) {
        if (length < 1)
            throw new IllegalArgumentException();
        this.text = Objects.requireNonNull(text);
        this.start = text.at(start);
        this.end = text.at(start+length);
        this.type = Objects.requireNonNull(type);
        this.value = text.substring(start, length);
    }
}
