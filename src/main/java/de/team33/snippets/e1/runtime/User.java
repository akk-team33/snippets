package de.team33.snippets.e1.runtime;

import de.team33.patterns.lazy.e1.Lazy;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Provides user related information from the runtime environment.
 */
public class User {

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
     * Returns a {@link Path} intended to refer a directory for storing configuration data.
     * The directory name results from the name of a given java package and starts with a period.
     * <p>
     * It is not checked whether the resulting path actually refers a directory or another file system object.
     */
    public static Path configPath(final Package p) {
        return home().resolve("." + p.getName());
    }

    /**
     * Returns a {@link Path} intended to refer a regular file for storing configuration data.
     * The file name results from the name of a given java class.
     * <p>
     * It is not checked whether the resulting path actually refers a directory or another file system object.
     */
    public static Path configFile(final Class<?> c) {
        final Package p = c.getPackage();
        final String name = c.getCanonicalName().substring(p.getName().length() + 1);
        return configPath(p).resolve(name);
    }

    private static Path newHome() {
        return Optional.ofNullable(System.getProperty(HOME_KEY))
                       .map(Paths::get)
                       .orElseThrow(User::missingHomeException)
                       .toAbsolutePath()
                       .normalize();
    }

    private static IllegalStateException missingHomeException() {
        return new IllegalStateException("Missing system property <" + HOME_KEY + ">");
    }
}
