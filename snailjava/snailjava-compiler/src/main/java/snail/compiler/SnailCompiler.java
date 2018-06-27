package snail.compiler;

import javax.annotation.Nonnull;
import java.nio.file.Paths;

public class SnailCompiler {
    public static void main(@Nonnull String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Bad argument count");
        }
        final SourceTree srcTree = new SourceTree(Paths.get(args[0]));
        srcTree.findModules()
                .forEach(srcTree::parseModule);
    }
}
