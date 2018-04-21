package snail.vm.values;

import org.junit.Test;
import snail.vm.util.GeneralException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static snail.vm.util.MiscUtils.mergeLists;

public class ValueTest {

    public static final List<SnailSimple> TEST_SIMPLE_1 =
            List.of(
                    new SnailString(""),
                    new SnailString("\\"),
                    new SnailString("\""),
                    new SnailInt(0),
                    new SnailInt(1),
                    new SnailInt(-1),
                    new SnailInt(Long.MIN_VALUE),
                    new SnailInt(Long.MAX_VALUE)
            );

    public static final List<SnailRef> TEST_SIMPLE_2 =
            Arrays.stream(SnailValueType.values())
                    .map(SnailValueType::ref)
                    .collect(Collectors.toList());

    public static final List<SnailSimple> TEST_SIMPLE = mergeLists(TEST_SIMPLE_1, TEST_SIMPLE_2);

    private static SnailList makeListA() {
        return SnailList.ofSimple(new SnailString("a"));
    }

    private static SnailList makeListAB() {
        return SnailList.ofSimple(new SnailString("a"), new SnailString("b"));
    }

    private static SnailList makeSimpleList() {
        final SnailList list = SnailList.make(null);
        list.add(null);
        TEST_SIMPLE.forEach(list::add);
        return list;
    }

    static final SnailRef TEST_CLASS1 = SnailRef.make("snail.test.class1");
    static final SnailRef TEST_CLASS1_ATTR1 = SnailRef.make("snail.test.class1.attr1");
    static final SnailRef TEST_CLASS1_ATTR2 = SnailRef.make("snail.test.class1.attr2");

    private static SnailObject makeSimpleObject() {
        final SnailObject object = SnailObject.make(TEST_CLASS1, null);
        object.set(TEST_CLASS1_ATTR1, new SnailString("Hello World!"));
        return object;
    }

    private static SnailList makeNestedList() {
        final SnailList list = SnailList.make(null);
        list.add(makeSimpleList());
        list.add(makeSimpleList());
        list.add(makeSimpleList());
        return list;
    }

    public static List<SnailValue> makeTestValues() {
        return mergeLists(
                TEST_SIMPLE,
                List.of(makeSimpleList()),
                List.of(makeNestedList()),
                List.of(makeListA()),
                List.of(makeListAB()),
                List.of(makeSimpleObject())
        );
    }

    @Test
    public void basicTests() {
        basicTest(null);
        basicTests(makeTestValues());
    }

    private static void basicTests(@Nonnull List<SnailValue> values) {
        values.forEach(ValueTest::basicTest);
        values.forEach(v1 -> values.forEach(v2 -> {
            final boolean eq = SnailValue.equals(v1, v2);
            final int r = SnailValue.compare(v1, v2);
            if (eq)
                assertEquals(0, r);
            else
                assertNotEquals(0, r);
            assertEquals(-SnailValue.compare(v1, v2), SnailValue.compare(v2, v1));
        }));
    }

    private static void basicTest(@Nullable SnailValue v) {
        assertEquals(0, SnailValue.compare(v, v));
        assertEquals(v == null ? 0 : -1, SnailValue.compare(null, v));
        assertNotNull(SnailValue.valueType(v).ref());
        assertNotNull(SnailValue.asPrintable(v));
        if (v != null)
            assertEquals(SnailValue.asPrintable(v), v.toString());
        final int hashCode = SnailValue.hashCode(v);
        if (v == null)
            assertEquals(0, hashCode);
    }

    @Test(expected = GeneralException.class)
    public void testBadTypeTag() {
        SnailValueType.of(Byte.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareSameTypeWrongType1() {
        new SnailString("").compareSameType(new SnailInt(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareSameTypeWrongType2() {
        SnailList.make(null).compareSameType(new SnailInt(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadRefName() {
        new SnailRef(null, "");
    }

    @Test
    public void testRefGetNameRelativeTo() {
        final SnailRef test = SnailRef.make("test");
        final SnailRef test_foo = SnailRef.make("test.foo");
        final SnailRef test_foo_bar = SnailRef.make("test.foo.bar");
        final SnailRef test_bar = SnailRef.make("test.bar");
        assertEquals(".foo", test_foo.getNameRelativeTo(test));
        assertEquals(".foo.bar", test_foo_bar.getNameRelativeTo(test));
        assertEquals(".bar", test_foo_bar.getNameRelativeTo(test_foo));
        assertEquals("test.foo", test_foo.getNameRelativeTo(test_bar));
    }

    @Test
    public void listCompare() {
        final SnailList LIST_A = makeListA();
        final SnailList LIST_A_B = makeListAB();
        assertEquals(-1, SnailValue.compare(LIST_A, LIST_A_B));
        assertEquals(1, SnailValue.compare(LIST_A_B, LIST_A));
        assertEquals(0, SnailValue.compare(LIST_A, LIST_A));
        assertEquals(0, SnailValue.compare(LIST_A_B, LIST_A_B));
    }
}
