package snail.common.values;

import snail.common.util.GeneralException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A list.
 */
public final class SnailList extends SnailComplex {
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
        return null;
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
            final SnailComplex v = ((SnailComplex) value).copyNonRoot(() -> this);
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

    private void remove(int index) {
        SnailComplex.deleteIfComplex(values.remove(index));
    }
}
