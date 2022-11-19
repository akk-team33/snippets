package de.team33.snippets.e1.runtime;

import de.team33.patterns.lazy.e1.Lazy;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides user related information from the runtime environment.
 */
public class User {

    private static final String DOT = ".";
    private static final String HOME_KEY = "user.home";
    private static final Lazy<Path> HOME = new Lazy<>(() -> Optional.ofNullable(System.getProperty(HOME_KEY))
                                                                    .map(Paths::get)
                                                                    .orElseThrow(User::missingHomeException)
                                                                    .toAbsolutePath()
                                                                    .normalize());

    /**
     * Returns an absolute, normalized {@link Path} to the user's home directory.
     */
    public static Path home() {
        return HOME.get();
    }

    /**
     * Returns a {@link Path} intended to refer a user related directory for storing configuration data.
     * The directory name results from the name of a given java package.
     * <p>
     * It is not checked whether the resulting path actually refers a directory or even exists.
     */
    public static Path configPath(final Package p) {
        return home().resolve(".etc").resolve(p.getName());
    }

    /**
     * Returns a {@link Path} intended to refer a user related regular file for storing configuration data.
     * The file name results from the name of a given java class.
     * <p>
     * It is not checked whether the resulting path actually refers a regular file or even exists.
     */
    public static Path configFile(final Class<?> c, final String... extensions) {
        final Package p = c.getPackage();
        final String name = c.getCanonicalName()
                             .substring(p.getName().length() + 1) + normal(extensions);
        return configPath(p).resolve(name);
    }

    private static String normal(final String... extensions) {
        final String result = Stream.of(extensions)
                                    .map(entry -> entry.split("[\\s.]+"))
                                    .flatMap(Stream::of)
                                    .filter(entry -> !"".equals(entry))
                                    .collect(Collectors.joining("."));
        return "".equals(result) ? result : ("." + result);
    }

    private static IllegalStateException missingHomeException() {
        return new IllegalStateException("Missing system property <" + HOME_KEY + ">");
    }
}
