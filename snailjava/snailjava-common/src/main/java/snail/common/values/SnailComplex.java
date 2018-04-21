package snail.common.values;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A complex value is stored on the heap.
 */
public abstract class SnailComplex implements SnailComplexParent, SnailHeapValue {
    private SnailComplexParent parent;
    private final Set<SnailHandle> ptrHandles = new HashSet<>();

    protected SnailComplex(@Nonnull SnailComplexParent parent) {
        this.parent = Objects.requireNonNull(parent);
    }

    public final SnailHandle makeHandle() {
        return new SnailHandle(this);
    }

    @Override
    public final SnailDocument document() {
        if (parent instanceof SnailDocument)
            return (SnailDocument) parent;
        return parent.document();
    }

    public final void addHandle(@Nonnull SnailHandle handle) {
        if (handle.deref() != this)
            throw new IllegalStateException();
        ptrHandles.add(handle);
    }

    public void removeHandle(@Nonnull SnailHandle handle) {
        if (handle.deref() != this)
            throw new IllegalStateException();
        ptrHandles.remove(handle);
    }

    @Override
    public SnailComplexParent parent() {
        return parent;
    }

    public void parent(@Nonnull SnailComplex newParent) {
        if (parent == newParent)
            return;
        final SnailDocument oldParent = parent == null ? null : (SnailDocument) parent;
        this.parent = newParent;
        if (oldParent != null)
            oldParent.transferHandlesTo(newParent.document());
    }

    public SnailComplex copyNonRoot(@Nonnull Supplier<SnailComplexParent> parentSupplier) {
        return isRoot() ? this : copy(parentSupplier.get());
    }

    protected abstract SnailComplex copy(@Nonnull SnailComplexParent newParent);

    private boolean isRoot() {
        return parent instanceof SnailDocument;
    }

    public final void delete() {
        ptrHandles.forEach(SnailHandle::free);
        doDelete();
    }

    protected abstract void doDelete();

    public static void deleteIfComplex(@Nullable SnailHeapValue value) {
        if (value instanceof SnailComplex)
            ((SnailComplex) value).delete();
    }

    @Override
    public final int compareSameType(@Nonnull SnailValue b) {
        if (valueType() != b.valueType())
            throw new IllegalArgumentException();
        return doCompare((SnailComplex) b);
    }

    protected abstract int doCompare(@Nonnull SnailComplex b);

    @Override
    public final boolean equals(Object obj) {
        return obj instanceof SnailValue && SnailValue.equals(this, (SnailValue) obj);
    }

    @Override
    public final String toString() {
        return asPrintable();
    }

    public int countHandles() {
        return ptrHandles.size();
    }

    @Override
    public void ensureMutable() {
        document().ensureMutable();
    }

    @Override
    public boolean mutable() {
        return document().mutable();
    }

    @Override
    public void seal() {
        document().seal();
    }
}
