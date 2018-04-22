package snail.vm.values;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A reference. This is a name aka symbol. It can have a parent. Hierarchical name segments are separated by dots.
 */
public final class SnailRef extends SnailSimple {
    private final SnailRef parent;
    private final String name;

    public SnailRef(@Nullable SnailRef parent, @Nonnull String name) {
        this.parent = parent;
        this.name = Objects.requireNonNull(name);
        if (!VALID_NAME_PATTERN.matcher(name).matches())
            throw new IllegalArgumentException();
    }

    private static final Pattern VALID_NAME_PATTERN =
            Pattern.compile("^([A-Za-z_][A-Za-z_0-9]*)$");

    public static SnailRef make(@Nonnull String name) {
        final String[] names = name.split("[.]");
        SnailRef cur = null;
        for (String segment : names)
            cur = new SnailRef(cur, segment);
        return cur;
    }

    public SnailRef child(@Nonnull String child) {
        return make(asPrintable() + "." + child);
    }

    @Override
    public SnailValueType valueType() {
        return SnailValueType.T_ref;
    }

    public String getNameRelativeTo(@Nonnull SnailRef context) {
        final String ourName = asPrintable(), contextName = context.asPrintable();
        if (ourName.startsWith(contextName + "."))
            return ourName.substring(contextName.length());
        return ourName;
    }

    @Override
    public String asPrintable() {
        return parent != null ? parent.asPrintable() + "." + name : name;
    }

    @Override
    protected int doCompare(@Nonnull SnailSimple b) {
        return asPrintable().compareTo(((SnailRef) b).asPrintable());
    }

    @Override
    public int doHashCode() {
        return Objects.hash(parent, name);
    }
}
