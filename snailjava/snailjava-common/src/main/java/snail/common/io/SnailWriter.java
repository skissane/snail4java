package snail.common.io;

import snail.common.util.GeneralException;
import snail.common.values.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.NavigableSet;
import java.util.Objects;

import static snail.common.util.CheckedException.handle;

public final class SnailWriter {
    private final DataOutputStream out;

    public SnailWriter(@Nonnull OutputStream out) {
        this(toDataOutputStream(out));
    }

    private static DataOutputStream toDataOutputStream(@Nonnull OutputStream out) {
        return out instanceof DataOutputStream ? (DataOutputStream) out : new DataOutputStream(out);
    }

    private SnailWriter(@Nonnull DataOutputStream out) {
        this.out = Objects.requireNonNull(out);
    }

    public static byte[] toBytes(@Nullable SnailValue value) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final SnailWriter writer = new SnailWriter(out);
        writer.write(value);
        return out.toByteArray();
    }

    private void write(@Nullable SnailValue value) {
        if (value == null)
            writeTag(SnailValueType.T_null);
        else if (value instanceof SnailHandle) {
            write(((SnailHandle) value).deref());
        } else {
            final SnailValueType valueType = value.valueType();
            switch (valueType) {
                case T_ref:
                    writeRef((SnailRef) value);
                    break;
                case T_int:
                    writeInt((SnailInt) value);
                    break;
                case T_string:
                    writeString((SnailString) value);
                    break;
                case T_object:
                    writeObject((SnailObject) value);
                    break;
                case T_list:
                    writeList((SnailList) value);
                    break;
                default:
                    throw new GeneralException("Cannot write value of type %s",
                            valueType.ref()
                    );
            }
        }
    }

    private void writeObject(@Nonnull SnailObject object) {
        try {
            writeRef(object.ofClass());
            final NavigableSet<SnailRef> attrs = object.attrs();
            out.writeInt(attrs.size());
            attrs.forEach(attr -> {
                writeRef(attr);
                write(object.get(attr));
            });
        } catch (IOException e) {
            throw handle(e);
        }

    }

    private void writeList(@Nonnull SnailList list) {
        try {
            final int size = list.size();
            out.writeInt(size);
            for (int i = 0; i < size; i++)
                write(list.get(i));
        } catch (IOException e) {
            throw handle(e);
        }
    }

    private void writeInt(@Nonnull SnailInt integer) {
        try {
            out.writeLong(integer.value());
        } catch (IOException e) {
            throw handle(e);
        }
    }

    private void writeString(@Nonnull SnailString str) {
        writeString(str.value());
    }

    private void writeRef(@Nonnull SnailRef ref) {
        final String name = ref.asPrintable();
        writeString(name);
    }

    private void writeString(@Nonnull String str) {
        try {
            final byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            out.writeInt(bytes.length);
            out.write(bytes);
        } catch (IOException e) {
            throw handle(e);
        }
    }

    private void writeTag(@Nonnull SnailValueType valueType) {
        try {
            out.write(valueType.ordinal());
        } catch (IOException e) {
            throw handle(e);
        }
    }
}
