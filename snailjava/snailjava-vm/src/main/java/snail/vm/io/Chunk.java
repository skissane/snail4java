package snail.vm.io;

import snail.vm.util.GeneralException;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * We use "chunks" as a generic container file format. This is inspired by the EA Interchange File Format (IFF), but
 * not intended to be in any way compatible with it.
 */
public final class Chunk {
    private static final Pattern VALID_TAG_PATTERN = Pattern.compile("^[A-Za-z0-9]{4}$");
    private final String tag;
    private final byte[] data;

    public static final String EOF_MARKER = "ENDS";

    public boolean isEofChunk() {
        return EOF_MARKER.equals(tag);
    }

    public static Chunk eof() {
        return new Chunk(EOF_MARKER, new byte[0]);
    }

    public Chunk(@Nonnull String tag, @Nonnull byte[] data) {
        if (!VALID_TAG_PATTERN.matcher(tag).matches())
            throw new GeneralException("Invalid chunk tag: '%s'", tag);
        this.tag = Objects.requireNonNull(tag);
        this.data = Objects.requireNonNull(data);

        if (isEofChunk() && data.length != 0)
            throw new GeneralException("Got 'ENDS' chunk with data length %d - should be zero", data.length);
    }

    public String tag() {
        return tag;
    }

    public byte[] data() {
        return data;
    }

    @Override
    public String toString() {
        return "Chunk[tag=" + tag + ":data.length=" + data.length + "]";
    }
}
