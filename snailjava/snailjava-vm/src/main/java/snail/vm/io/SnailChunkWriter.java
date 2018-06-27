package snail.vm.io;

import snail.vm.util.GeneralException;

import javax.annotation.Nonnull;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static snail.vm.util.CheckedException.handle;

public class SnailChunkWriter implements AutoCloseable {
    private final DataOutputStream out;
    private volatile boolean closed;

    public SnailChunkWriter(@Nonnull OutputStream out) {
        this(toDataOutputStream(out));
    }

    private SnailChunkWriter(@Nonnull DataOutputStream out) {
        this.out = Objects.requireNonNull(out);
    }

    private static DataOutputStream toDataOutputStream(@Nonnull OutputStream out) {
        return out instanceof DataOutputStream ? (DataOutputStream) out : new DataOutputStream(out);
    }

    @Override
    public void close() {
        try {
            if (closed)
                return;
            doWrite(Chunk.eof());
            out.close();
            closed = true;
        } catch (IOException e) {
            throw handle(e);
        }
    }

    private void doWrite(@Nonnull Chunk chunk) {
        try {
            out.write(chunk.tag().getBytes(StandardCharsets.US_ASCII));
            out.writeInt(chunk.data().length);
            out.write(chunk.data());
        } catch (IOException e) {
            throw handle(e);
        }
    }

    public void write(@Nonnull Chunk chunk) {
        if (chunk.isEofChunk()) {
            close();
        } else {
            if (closed)
                throw new GeneralException("Attempt to write chunk to closed chunk writer");
            doWrite(chunk);
        }
    }
}
