package snail.common.io;

import snail.common.util.GeneralException;
import snail.common.values.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static snail.common.util.CheckedException.handle;

public class SnailReader {
    private final DataInputStream in;

    public SnailReader(@Nonnull InputStream in) {
        this(toDataInputStream(in));
    }

    private static DataInputStream toDataInputStream(@Nonnull InputStream in) {
        return in instanceof DataInputStream ? (DataInputStream) in : new DataInputStream(in);
    }

    private SnailReader(@Nonnull DataInputStream in) {
        this.in = Objects.requireNonNull(in);
    }

    public static SnailHeapValue fromBytes(@Nonnull byte[] data) {
        final ByteArrayInputStream in = new ByteArrayInputStream(data);
        final SnailReader reader = new SnailReader(in);
        return reader.read(null);
    }

    private SnailHeapValue read(@Nullable SnailComplex context) {
        try {
            final SnailValueType type = readTag();
            switch (type) {
                case T_null:
                    return null;
                case T_int:
                    return new SnailInt(in.readLong());
                case T_string:
                    return new SnailString(readString());
                case T_ref:
                    return SnailRef.make(readString());
                case T_list:
                    return readList(context);
                case T_object:
                    return readObject(context);
                default:
                    throw new GeneralException("Don't know how to read type %s", type.ref());
            }
        } catch (IOException e) {
            throw handle(e);
        }
    }

    private SnailHeapValue readObject(@Nullable SnailComplex context) {
        try {
            final SnailRef ofClass = SnailRef.make(readString());
            final SnailObject object = SnailObject.make(ofClass,context);
            final int size = in.readInt();
            for (int i = 0; i < size ; i++) {
                final SnailRef attr = SnailRef.make(readString());
                object.set(attr, read(object));
            }
            return object;
        } catch (IOException e) {
            throw handle(e);
        }

    }

    private SnailHeapValue readList(@Nullable SnailComplex context) {
        try {
            final SnailList list = SnailList.make(context);
            final int size = in.readInt();
            for (int i = 0; i < size; i++) {
                list.add(read(list));
            }
            return list;
        } catch (IOException e) {
            throw handle(e);
        }
    }

    private String readString() {
        try {
            final int size = in.readInt();
            final byte[] data = new byte[size];
            in.readFully(data);
            return new String(data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw handle(e);
        }
    }

    private SnailValueType readTag() {
        try {
            return SnailValueType.of(in.readByte());
        } catch (IOException e) {
            throw handle(e);
        }
    }
}
