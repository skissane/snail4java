package snail.vm.bytecode;

import snail.vm.values.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a single Snail method.
 */
public class SnailMethod {
    private static final SnailRef CLASS_vmMethod = SnailRef.make("snail.vmMethod");
    private static final SnailRef ATTR_arguments = CLASS_vmMethod.child("arguments");
    private static final SnailRef ATTR_variables = CLASS_vmMethod.child("variables");
    private static final SnailRef ATTR_constantPool = CLASS_vmMethod.child("constantPool");
    private static final SnailRef ATTR_wCount = CLASS_vmMethod.child("wCount");
    private static final SnailRef ATTR_yCount = CLASS_vmMethod.child("yCount");
    private static final SnailRef ATTR_instructions = CLASS_vmMethod.child("instructions");

    public SnailObject asObject(@Nullable SnailComplex parent) {
        final SnailObject o = SnailObject.make(CLASS_vmMethod, parent);
        o.set(ATTR_arguments, arguments);
        o.set(ATTR_variables, variables);
        o.set(ATTR_wCount, new SnailInt(wCount));
        o.set(ATTR_yCount, new SnailInt(yCount));
        o.set(ATTR_constantPool, constantPool);
        o.set(ATTR_instructions, new SnailBinary(instructions));
        return o;
    }

    private final SnailList arguments;
    private final SnailList variables;

    /**
     * How many W and Y registers to allocate. The X count is determined by arguments and the Z count by the
     * number of variables.
     */
    private final int wCount, yCount;

    /**
     * Pool of constants accessible to this method.
     */
    private final SnailList constantPool;

    /**
     * Contains instructions.
     */
    private final byte[] instructions;

    public SnailMethod(@Nonnull SnailList arguments, @Nonnull SnailList variables,
                       @Nonnull SnailList constantPool,
                       int wCount, int yCount,
                       @Nonnull byte[] instructions) {
        this.wCount = wCount;
        this.yCount = yCount;
        if (wCount < 0 || yCount < 0 ||
                wCount > 255 || arguments.size() > 255 || yCount > 255 || variables.size() > 255)
            throw new IllegalArgumentException();
        this.arguments = arguments;
        this.arguments.seal();
        this.variables = variables;
        this.variables.seal();
        this.constantPool = Objects.requireNonNull(constantPool);
        this.constantPool.seal();
        this.instructions = Objects.requireNonNull(instructions);
        if (totalRegisters() > 255)
            throw new IllegalArgumentException("Too many registers");
    }

    public int registerCount(@Nonnull RegisterType type) {
        switch (type) {
            case W:
                return wCount;
            case X:
                return arguments.size();
            case Y:
                return yCount;
            case Z:
                return variables.size();
            default:
                throw new IllegalArgumentException();
        }
    }

    public int totalRegisters() {
        return Arrays.stream(RegisterType.values())
                .mapToInt(this::registerCount)
                .sum();
    }
}
