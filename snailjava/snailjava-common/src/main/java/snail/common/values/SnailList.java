package snail.common.values;

import snail.common.util.GeneralException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A list.
 */
public final class SnailList extends SnailComplex implements Iterable<SnailHeapValue> {
    private final List<SnailHeapValue> values = new ArrayList<>();

    public SnailList(@Nonnull SnailComplexParent parent) {
        super(parent);
    }

    @Override
    public SnailValueType valueType() {
        return SnailValueType.T_list;
    }

    @Override
    public String asPrintable() {
        final StringBuilder r = new StringBuilder();
        r.append("{");
        values.forEach(value -> {
            if (r.length() > 1)
                r.append(" ");
            r.append(SnailValue.asPrintable(value));
        });
        r.append("}");
        return r.toString();
    }

    public int size() {
        return values.size();
    }

    public SnailHeapValue get(int index) {
        return values.get(index);
    }

    public static SnailList make(@Nullable SnailComplex parent) {
        if (parent == null) {
            final SnailDocument doc = new SnailDocument();
            final SnailList list = new SnailList(doc);
            doc.root(list);
            return list;
        }
        return new SnailList(parent);
    }

    public void add(@Nullable SnailHeapValue value) {
        if (value == null || value instanceof SnailSimple)
            values.add(value);
        else if (value instanceof SnailComplex) {
            final SnailComplex cv = (SnailComplex) value;
            final SnailComplex v = cv.parent() == this ? cv : cv.copyNonRoot(() -> this);
            v.parent(this);
            values.add(v);
        } else
            throw new GeneralException("Cannot add %s to list", value);
    }

    @Override
    protected SnailComplex copy(@Nonnull SnailComplexParent newParent) {
        final SnailList copy = new SnailList(newParent);
        for (int i = 0; i < size(); i++)
            copy.values.add(SnailValue.copy(get(i), copy));
        return copy;
    }

    @Override
    protected void doDelete() {
        while (size() != 0)
            remove(0);
    }

    public void remove(int index) {
        SnailComplex.deleteIfComplex(values.remove(index));
    }

    @Override
    protected int doCompare(@Nonnull SnailComplex b) {
        final SnailList la = this;
        final SnailList lb = (SnailList) b;
        final Iterator<SnailHeapValue> ia = la.iterator();
        final Iterator<SnailHeapValue> ib = lb.iterator();
        while (true) {
            final boolean ha = ia.hasNext();
            final boolean hb = ib.hasNext();
            if (!ha && !hb)
                return 0;
            if (!ha)
                return -1;
            if (!hb)
                return 1;
            final SnailHeapValue ea = ia.next();
            final SnailHeapValue eb = ib.next();
            final int r = SnailValue.compare(ea, eb);
            if (r != 0)
                return r;
        }
    }

    @Override
    public Iterator<SnailHeapValue> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size();
            }

            @Override
            public SnailHeapValue next() {
                return get(index++);
            }
        };
    }

    @Override
    public int doHashCode() {
        return values.hashCode();
    }

    public static SnailList ofSimple(SnailSimple... values) {
        final SnailList list = SnailList.make(null);
        for (SnailSimple value : values)
            list.add(value);
        return list;
    }

    public SnailList addNewList() {
        final SnailList list = SnailList.make(this);
        add(list);
        return list;
    }

    public SnailList addSimpleList(SnailSimple... values) {
        final SnailList list = SnailList.make(this);
        for (SnailSimple value : values)
            list.add(value);
        add(list);
        return list;
    }
}
