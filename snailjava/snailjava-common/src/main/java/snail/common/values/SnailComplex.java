package snail.common.values;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
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
}
