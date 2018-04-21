package snail.vm.values;

import javax.annotation.Nonnull;

/**
 * 64-bit signed integer.
 */
public final class SnailInt extends SnailSimple {
    private final long value;

    public SnailInt(long value) {
        this.value = value;
    }

    @Override
    public SnailValueType valueType() {
        return SnailValueType.T_int;
    }

    @Override
    public String asPrintable() {
        return Long.toString(value);
    }

    public long value() {
        return value;
    }

    @Override
    protected int doCompare(@Nonnull SnailSimple b) {
        return Long.compare(value, ((SnailInt)b).value);
    }

    @Override
    public int doHashCode() {
        return Long.hashCode(value);
    }
}
