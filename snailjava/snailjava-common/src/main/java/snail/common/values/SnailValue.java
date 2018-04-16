package snail.common.values;

import snail.common.util.GeneralException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static snail.common.util.MiscUtils.uncheckedCast;

/**
 * All Snail values, whether heap or register.
 */
public interface SnailValue {
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
}
