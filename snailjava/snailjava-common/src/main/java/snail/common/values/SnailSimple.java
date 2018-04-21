package snail.common.values;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Simple values can be used on heap or in registers. They lack internal structure.
 */
public abstract class SnailSimple implements SnailHeapValue, SnailRegisterValue {
    @Override
    public final String toString() {
        return asPrintable();
    }

    @Override
    public final int compareSameType(@Nonnull SnailValue b) {
        if (valueType() != b.valueType())
            throw new IllegalArgumentException();
        return doCompare((SnailSimple) b);
    }

    protected abstract int doCompare(@Nonnull SnailSimple b);

    @Override
    public final boolean equals(Object obj) {
        return obj instanceof SnailValue && SnailValue.equals(this, (SnailValue) obj);
    }
}
