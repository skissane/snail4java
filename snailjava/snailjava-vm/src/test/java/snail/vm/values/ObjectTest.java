package snail.vm.values;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static snail.vm.values.ValueTest.TEST_CLASS1;
import static snail.vm.values.ValueTest.TEST_CLASS1_ATTR1;
import static snail.vm.values.ValueTest.TEST_CLASS1_ATTR2;

public class ObjectTest {
    @Test
    public void testObject1() {
        final SnailObject outer = SnailObject.make(TEST_CLASS1, null);
        SnailObject inner = outer;
        for (int i = 0; i < 10; i++) {
            SnailObject prev = inner;
            inner = new SnailObject(TEST_CLASS1, prev);
            prev.set(TEST_CLASS1_ATTR1, new SnailString("X" + i));
            prev.set(TEST_CLASS1_ATTR2, inner);
        }
        assertEquals(outer.document().valueType(), outer.valueType());
        assertEquals(SnailValue.hashCode(outer.document()), SnailValue.hashCode(outer));
        assertEquals(2, outer.attrs().size());
        assertSame(outer.document(), inner.document());
        assertSame(outer.document(), outer.parent());
        assertEquals(0, inner.countHandles());
        assertEquals(0, outer.countHandles());
        assertEquals(0, inner.document().countHandles());
        final SnailHandle innerHandle = inner.makeHandle();
        assertEquals(1, inner.countHandles());
        assertEquals(0, outer.countHandles());
        assertEquals(1, inner.document().countHandles());
        assertSame(innerHandle.deref(), inner);
        assertSame(innerHandle.document(), outer.parent());
        assertTrue(SnailValue.equals(innerHandle, inner));
        outer.remove(TEST_CLASS1_ATTR2);
        assertNull(innerHandle.deref());
        assertNull(innerHandle.document());
        assertEquals(0, inner.countHandles());
        assertEquals(0, outer.countHandles());
        assertEquals(0, inner.document().countHandles());
    }
}