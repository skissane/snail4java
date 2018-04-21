package snail.common.values;

/**
 * Any value which can be a parent of a complex value. Either a document (root value) or another complex value
 * (intermediate value). Every complex value must have a parent.
 */
public interface SnailComplexParent {
    SnailDocument document();

    SnailComplexParent parent();

    void ensureMutable();

    boolean mutable();

    void seal();
}
