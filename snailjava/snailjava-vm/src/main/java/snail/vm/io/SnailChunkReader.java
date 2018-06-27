package snail.vm.io;

import snail.vm.util.GeneralException;

import javax.annotation.Nonnull;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static snail.vm.util.CheckedException.handle;

public class SnailChunkReader {
    private final DataInputStream in;

    public SnailChunkReader(@Nonnull InputStream in) {
        this(toDataInputStream(in));
    }

    private SnailChunkReader(@Nonnull DataInputStream in) {
        this.in = Objects.requireNonNull(in);
    }

    private static DataInputStream toDataInputStream(@Nonnull InputStream in) {
        return in instanceof DataInputStream ? (DataInputStream) in : new DataInputStream(in);
    }

    private volatile boolean end = false;

    public Chunk read() {
        try {
            if (end)
                return null;
            final Chunk chunk = doReadChunk();
            if (chunk.isEofChunk()) {
                if (in.read() != -1)
                    throw new GeneralException("Found junk data after 'ENDS' chunk");
                end = true;
            }
            return chunk;
        } catch (IOException e) {
            throw handle(e);
        }
    }

    private Chunk doReadChunk() {
        try {
            final String tag = readTag();
            final int length = in.readInt();
            final byte[] data = new byte[length];
            in.readFully(data);
            return new Chunk(tag, data);
        } catch (IOException e) {
            throw handle(e);
        }
    }

    private String readTag() {
        try {
            final byte[] buf = new byte[4];
            in.readFully(buf);
            return new String(buf, StandardCharsets.US_ASCII);
        } catch (IOException e) {
            throw handle(e);
        }
    }
}
