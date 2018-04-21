package snail.common.values;

import snail.common.util.GeneralException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Objects;

import static snail.common.util.MiscUtils.uncheckedCast;

/**
 * All Snail values, whether heap or register.
 */
public interface SnailValue extends Comparable<SnailValue> {
    SnailValueType valueType();

    String asPrintable();

    static String asPrintable(@Nullable SnailValue v) {
        return v == null ? "*" : v.asPrintable();
    }

    static <T extends SnailValue> T copy(@Nullable T value, @Nonnull SnailComplexParent newParent) {
        if (value == null || (value instanceof SnailSimple))
            return value;
        if (value instanceof SnailHandle)
            return uncheckedCast(copy(((SnailHandle) value).deref(), newParent));
        if (value instanceof SnailComplex)
            return uncheckedCast(((SnailComplex) value).copy(newParent));
        throw new GeneralException("Values of class %s are unsupported", value.getClass().getName());
    }

    static SnailValueType valueType(@Nullable SnailValue value) {
        return value == null ? SnailValueType.T_null : value.valueType();
    }

    @Override
    default int compareTo(@Nonnull SnailValue o) {
        return SnailValue.compare(this, o);
    }

    static int compare(@Nullable SnailValue a, @Nullable SnailValue b) {
        a = unwrap(a);
        b = unwrap(b);
        if (a == null && b == null)
            return 0;
        if (a == null)
            return -1;
        if (b == null)
            return 1;
        final SnailValueType ta = valueType(a);
        final SnailValueType tb = valueType(b);
        if (ta != tb)
            return ta.compareTo(tb);
        return a.compareSameType(b);
    }

    static SnailValue unwrap(@Nullable SnailValue v) {
        if (v instanceof SnailDocument)
            return ((SnailDocument) v).root();
        if (v instanceof SnailHandle)
            return ((SnailHandle) v).deref();
        return v;
    }

    int compareSameType(@Nonnull SnailValue b);

    static boolean equals(@Nullable SnailValue a, @Nullable SnailValue b) {
        return compare(a,b)==0;
    }

    static int hashCode(@Nullable SnailValue v) {
        if (v == null)
            return 0;
        return Objects.hash(v.valueType(), v.doHashCode());
    }

    int doHashCode();
}
