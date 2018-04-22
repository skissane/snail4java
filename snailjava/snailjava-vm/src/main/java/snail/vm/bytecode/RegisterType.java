package snail.vm.bytecode;

/**
 * Types of registers.
 */
public enum RegisterType {
    /**
     * X registers are argument registers. They store incoming arguments to this method.
     */
    X,
    /**
     * Z registers store local variables.
     */
    Z,
    /**
     * W registers are work registers. They store temporary values.
     * Y registers are just aliases for W registers.
     */
    W
}