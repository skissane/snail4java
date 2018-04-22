package snail.vm.values;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A string value.
 */
public final class SnailBinary extends SnailSimple {
    private final byte[] value;

    public SnailBinary(@Nonnull byte[] value) {
        this.value = value.clone();
    }

    @Override
    public SnailValueType valueType() {
        return SnailValueType.T_binary;
    }

    @Override
    public String asPrintable() {
        return "$(" + toHex() + ")";
    }

    private String toHex() {
        return IntStream.range(0, size())
                .map(this::byteAt)
                .mapToObj(v -> String.format("%02x", v))
                .collect(Collectors.joining());
    }

    private byte byteAt(int i) {
        return value[i];
    }

    private int size() {
        return value.length;
    }

    public byte[] value() {
        return value.clone();
    }

    @Override
    protected int doCompare(@Nonnull SnailSimple b) {
        return Arrays.compare(value, ((SnailBinary) b).value);
    }

    @Override
    public int doHashCode() {
        return Arrays.hashCode(value);
    }
}
