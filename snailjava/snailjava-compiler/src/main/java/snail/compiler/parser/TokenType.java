package snail.compiler.parser;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public enum TokenType {
    TT_integer("(0|-?[1-9][0-9]*)"),
    TT_hex("0x([0-9a-fA-F]{2})+"),
    TT_string("\"([^\\\\\"]|\\\\[\"nrt])*\""),
    TT_binary("$\\(([0-9a-fA-F]{2})*\\)"),
    TT_symbol("[.$]?([a-zA-Z_][a-zA-Z_]*#)?[a-zA-Z_][a-zA-Z_]*([.]([a-zA-Z_][a-zA-Z_]*#)[a-zA-Z_][a-zA-Z_]*)*"),
    TT_listBegin("[{]"),
    TT_listEnd("[}]"),
    TT_objectBegin("\\["),
    TT_objectEnd("]"),
    TT_expBegin("\\("),
    TT_expEnd("\\)"),
    TT_directiveBegin("#\\[");

    final Pattern pattern;

    TokenType(@Nonnull String pattern) {
        this.pattern = Pattern.compile("^" + pattern);
    }

    public Pattern pattern() {
        return pattern;
    }
}
