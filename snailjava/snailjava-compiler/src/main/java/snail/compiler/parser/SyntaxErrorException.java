package snail.compiler.parser;

import javax.annotation.Nonnull;

public class SyntaxErrorException extends RuntimeException {
    public SyntaxErrorException(@Nonnull Text.Position at, @Nonnull String msg) {
        super(at.where(msg));
    }
}
