package snail.common.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class MiscUtils {
    private MiscUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T uncheckedCast(@Nullable Object o) {
        return (T) o;
    }

    @SafeVarargs
    public static <T> List<T> mergeLists(@Nonnull List<? extends T>... lists) {
        return Arrays.stream(lists)
                .flatMap(Collection::stream)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }
}
