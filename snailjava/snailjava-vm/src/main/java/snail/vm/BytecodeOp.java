package snail.vm;

/**
 * Byte code opcodes.
 */
public enum BytecodeOp {
    CALL,
    CONSTANT,
    JUMP,
    JUMPIF,
    LISTADD,
    LISTDEL,
    LISTGET,
    LISTNEW,
    LISTSET,
    MOVE,
    OBJDEL,
    OBJGET,
    OBJNEW,
    OBJSET,
    RET;
}
