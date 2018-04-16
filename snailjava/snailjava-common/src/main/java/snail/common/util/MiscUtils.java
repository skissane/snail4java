package snail.common.util;

public final class MiscUtils {
    private MiscUtils() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T uncheckedCast(Object o) {
        return (T) o;
    }
}
