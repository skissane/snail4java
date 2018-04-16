package snail.common.values;

/**
 * Simple values can be used on heap or in registers. They lack internal structure.
 */
public abstract class SnailSimple implements SnailHeapValue, SnailRegisterValue {
    @Override
    public String toString() {
        return asPrintable();
    }
}
