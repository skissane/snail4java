package snail.common.values;

import snail.common.util.GeneralException;

public enum SnailValueType {
    T_null,
    T_ref,
    T_int,
    T_string,
    T_list,
    T_object,
    T_resource;

    private static final String PREFIX = "T_";
    private final SnailRef ref;

    SnailValueType() {
        this.ref = SnailRef.make("snail.type." + name().substring(PREFIX.length()));
    }

    public SnailRef ref() {
        return ref;
    }

    public static SnailValueType of(int tag) {
        for (SnailValueType type : SnailValueType.values())
            if (type.ordinal() == tag)
                return type;
        throw new GeneralException("Unrecognised type tag 0x%02X", tag);
    }
}
