package snail.compiler.parser;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;

public class Tokenizer implements Iterator<Token> {
    private final Text text;
    private int pos;

    private boolean isEOF() {
        return pos >= text.length();
    }

    public Tokenizer(@Nonnull Text text) {
        this.text = text;
    }

    @Override
    public boolean hasNext() {
        skipWSP();
        return !isEOF();
    }

    private void skipWSP() {
        while (Character.isWhitespace(text.charAt(pos)))
            pos++;
    }

    @Override
    public Token next() {
        if (!hasNext())
            throw new NoSuchElementException();
        final String target = text.substring(pos);
        for (TokenType tt : TokenType.values()) {
            final Matcher m = tt.pattern().matcher(target);
            if (!m.lookingAt())
                continue;
            final int length = m.group(0).length();
            final Token token = text.tokenAt(pos, tt, length);
            pos += length;
            return token;
        }
        throw text.syntaxError(pos, "invalid token");
    }
}
