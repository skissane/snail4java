package snail.common.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class GeneralException extends RuntimeException {
    public GeneralException(@Nonnull String format, @Nullable Object... args) {
        super(String.format(format, args));
    }
}
