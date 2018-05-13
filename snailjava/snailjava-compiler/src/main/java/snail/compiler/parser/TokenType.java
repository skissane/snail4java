package snail.compiler.parser;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

import static snail.compiler.parser.CommonSyntax.P_NAME;
import static snail.compiler.parser.CommonSyntax.P_PATH;

public enum TokenType {
    TT_integer("(0|-?[1-9][0-9]*)"),
    TT_hex("0x([0-9a-fA-F]+)"),
    TT_string("\"([^\\\\\"]|\\\\[\"nrt])*\""),
    TT_binary("$\\(([0-9a-fA-F]{2})*\\)"),
    TT_variable("[$]" + P_NAME),
    TT_symbol(P_PATH),
    TT_listBegin("[{]"),
    TT_listEnd("[}]"),
    TT_objectBegin("\\["),
    TT_objectEnd("]"),
    TT_expBegin("\\("),
    TT_expEnd("\\)"),
    TT_directiveBegin("#\\[");

    private final Pattern pattern;

    TokenType(@Nonnull String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public Pattern pattern() {
        return pattern;
    }
}
