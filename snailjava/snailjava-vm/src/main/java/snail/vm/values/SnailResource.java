package snail.vm.values;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * A resource is a special type of simple value pointing to some external resource, such as file.
 */
public abstract class SnailResource extends SnailSimple {
    private final SnailRef type;

    protected SnailResource(@Nonnull SnailRef type) {
        this.type = Objects.requireNonNull(type);
    }

    @Override
    public SnailValueType valueType() {
        return SnailValueType.T_resource;
    }
}
