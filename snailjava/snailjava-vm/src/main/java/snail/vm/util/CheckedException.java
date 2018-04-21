package snail.vm.util;

import javax.annotation.Nonnull;
import java.util.Objects;

public class CheckedException extends RuntimeException {
    private CheckedException(@Nonnull Throwable e) {
        super(Objects.requireNonNull(e));
    }

    public static RuntimeException handle(@Nonnull Throwable e) {
        if (e instanceof Error)
            throw (Error) e;
        return e instanceof RuntimeException ? (RuntimeException) e : new CheckedException(e);
    }
}
