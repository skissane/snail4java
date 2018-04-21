package snail.common.io;

import org.junit.Test;
import snail.common.values.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;
import static snail.common.util.CheckedException.handle;

public class IOTest {

    private static void roundTripTest(@Nullable SnailValue value1) {
        final byte[] data = SnailWriter.toBytes(value1);
        final SnailHeapValue value2 = SnailReader.fromBytes(data);
        assertEquals(0, SnailValue.compare(value1, value2));
        assertTrue(SnailValue.equals(value1, value2));
        assertEquals(value1, value2);
    }

    @Test
    public void roundTripSimple() {
        roundTripTest(null);
        ValueTest.makeTestValues().forEach(IOTest::roundTripTest);
    }

    @SuppressWarnings("SameParameterValue")
    private static void expecting(@Nonnull Class<? extends Throwable> cls, @Nonnull Runnable what) {
        try {
            what.run();
            fail("expected exception: " + cls.getName());
        } catch (Throwable got) {
            if (cls.isInstance(got))
                return;
            throw got;
        }
    }

    @Test
    public void junkAtEndTest() {
        ValueTest.makeTestValues().forEach(value -> {
            try {
                final ByteArrayOutputStream bout = new ByteArrayOutputStream();
                bout.write(SnailWriter.toBytes(value));
                bout.write(0);
                final byte[] data = bout.toByteArray();
                expecting(ExpectedEOFException.class, () -> SnailReader.fromBytes(data));
            } catch (IOException e) {
                throw handle(e);
            }
        });
    }
}