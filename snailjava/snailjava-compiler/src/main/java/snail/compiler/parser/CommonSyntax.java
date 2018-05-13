package snail.compiler.parser;

public class CommonSyntax {
    private CommonSyntax() {
    }

    static final String P_NAME = "[a-zA-Z_][a-zA-Z_]*";
    static final String P_MAYBE_TAGGED = "(" + P_NAME + "#)?(" + P_NAME + ")";
    static final String P_PATH = "[.]?" + P_MAYBE_TAGGED + "([.]" + P_MAYBE_TAGGED + ")*";
}