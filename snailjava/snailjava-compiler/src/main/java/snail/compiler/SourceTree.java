package snail.compiler;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static snail.vm.util.CheckedException.handle;

public class SourceTree {
    private final Path root;

    public SourceTree(@Nonnull Path root) {
        this.root = Objects.requireNonNull(root);
    }

    public SortedSet<String> findModules() {
        try (Stream<Path> paths = Files.walk(root, FileVisitOption.FOLLOW_LINKS)) {
            return paths
                    .filter(p -> Files.isRegularFile(p))
                    .filter(p -> p.getFileName().toString().endsWith(".snail"))
                    .map(SourceTree::pathToString)
                    .map(s -> s.substring(0, s.length() - ".snail".length()))
                    .map(s -> s.replace('/', '.'))
                    .collect(Collectors.toCollection(TreeSet::new));
        } catch (IOException e) {
            throw handle(e);
        }
    }

    private static String pathToString(@Nonnull Path p) {
        return StreamSupport.stream(p.spliterator(), false)
                .map(Path::toString)
                .collect(Collectors.joining("/"));
    }

    public void parseModule(@Nonnull String moduleName) {
        if (moduleName.startsWith(".") || moduleName.endsWith(".") || moduleName.contains("..") ||
                moduleName.contains("/") || moduleName.contains("\\"))
            throw new IllegalArgumentException("Bad module name '" + moduleName + "'");
        final Path modulePath = root.resolve(moduleName.replace('.', '/') + ".snail");
        if (!Files.isRegularFile(modulePath))
            throw new IllegalArgumentException("No such module '" + moduleName + "'");
    }
}
