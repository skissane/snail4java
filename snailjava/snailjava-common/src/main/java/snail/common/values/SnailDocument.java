package snail.common.values;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Every complex value belongs to a document. The document is the root of the complex value tree.
 */
public final class SnailDocument implements SnailComplexParent, SnailValue {
    private SnailComplex root;
    private final Set<SnailHandle> docHandles = new HashSet<>();

    @Override
    public SnailValueType valueType() {
        return root == null ? SnailValueType.T_null : root.valueType();
    }

    public SnailComplex root() {
        return root;
    }

    public void root(@Nonnull SnailComplex root) {
        if (this.root != null)
            throw new IllegalStateException();
        this.root = Objects.requireNonNull(root);
    }

    public void addHandle(@Nonnull SnailHandle handle) {
        if (this != handle.document())
            throw new IllegalStateException();
        this.docHandles.add(handle);
    }

    public void removeHandle(@Nonnull SnailHandle handle) {
        if (this != handle.document())
            throw new IllegalStateException();
        this.docHandles.remove(handle);
    }

    @Override
    public SnailDocument document() {
        return this;
    }

    @Override
    public String asPrintable() {
        return SnailValue.asPrintable(root);
    }

    @Override
    public SnailComplexParent parent() {
        return null;
    }

    public void transferHandlesTo(@Nonnull SnailDocument newDocument) {
        root = null;
        docHandles.forEach(handle -> handle.transferDocument(this, newDocument));
        assert docHandles.isEmpty();
    }

    @Override
    public int compareSameType(@Nonnull SnailValue b) {
        if (valueType() != b.valueType())
            throw new IllegalArgumentException();
        return SnailValue.compare(root, ((SnailDocument) b).root);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SnailValue && SnailValue.equals(this, (SnailValue) obj);
    }

    @Override
    public int doHashCode() {
        return root == null ? 0 : root.doHashCode();
    }

    @Override
    public String toString() {
        return asPrintable();
    }

    public int countHandles() {
        return docHandles.size();
    }
}
