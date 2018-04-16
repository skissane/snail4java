package snail.common.values;

import javax.annotation.Nonnull;

/**
 * Handles live in VM registers. They are pointers to complex values on heap.
 */
public final class SnailHandle implements SnailRegisterValue {
    private SnailDocument doc;
    private SnailComplex ptr;

    SnailHandle(@Nonnull SnailComplex complex) {
        this.ptr = complex;
        this.doc = complex.document();
        ptr.addHandle(this);
        doc.addHandle(this);
    }

    @Override
    public SnailValueType valueType() {
        return ptr == null ? SnailValueType.T_null : ptr.valueType();
    }

    public SnailComplex deref() {
        return ptr;
    }

    @Override
    public String asPrintable() {
        return SnailValue.asPrintable(ptr);
    }

    public SnailDocument document() {
        return doc;
    }

    public void free() {
        if (ptr != null)
            ptr.removeHandle(this);
        if (doc != null)
            doc.removeHandle(this);
        ptr = null;
        doc = null;
    }

    public void transferDocument(@Nonnull SnailDocument from, @Nonnull SnailDocument to) {
        if (from == to)
            throw new IllegalArgumentException();
        if (doc != from)
            throw new IllegalArgumentException();
        doc.removeHandle(this);
        doc = to;
        doc.addHandle(this);
    }
}
