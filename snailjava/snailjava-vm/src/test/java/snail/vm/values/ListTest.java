package snail.vm.values;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class ListTest {
    @Test
    public void listTest1() {
        final SnailList outer = SnailList.make(null);
        assertEquals(0, outer.size());
        final SnailList inner = SnailList.make(outer);
        outer.add(inner);
        assertEquals(1, outer.size());
        assertSame(inner, outer.get(0));
        assertSame(outer.document(), inner.document());
        assertSame(outer, inner.parent());
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
        outer.remove(0);
        assertNull(innerHandle.deref());
        assertNull(innerHandle.document());
        assertEquals(0, inner.countHandles());
        assertEquals(0, outer.countHandles());
        assertEquals(0, inner.document().countHandles());
    }

    @Test
    public void listTest2() {
        final SnailList outer = SnailList.make(null);
        SnailList inner = outer;
        for (int i = 0; i < 10; i++) {
            inner = inner.addNewList();
            inner.add(new SnailInt(i));
        }
        assertEquals(outer.document().valueType(), outer.valueType());
        assertEquals(SnailValue.hashCode(outer.document()), SnailValue.hashCode(outer));
        assertEquals(1, outer.size());
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
        outer.remove(0);
        assertNull(innerHandle.deref());
        assertNull(innerHandle.document());
        assertEquals(0, inner.countHandles());
        assertEquals(0, outer.countHandles());
        assertEquals(0, inner.document().countHandles());
    }

    @Test
    public void listTest3() {
        final SnailList x = SnailList.make(null);
        final SnailList y = SnailList.make(null);
        x.addSimpleList(ValueTest.TEST_SIMPLE.toArray(new SnailSimple[0]));
        final SnailList x0 = (SnailList) x.get(0);
        y.add(x0);
        final SnailList y0 = (SnailList) y.get(0);
        assertEquals(x0, y0);
        assertEquals(x0.size(), y0.size());
        assertNotSame(x0, y0);
        assertNotSame(x0.document(), y0.document());
        final String test = UUID.randomUUID().toString();
        y0.add(new SnailString(test));
        assertNotEquals(x0, y0);
        assertNotEquals(x0.size(), y0.size());
    }
}
