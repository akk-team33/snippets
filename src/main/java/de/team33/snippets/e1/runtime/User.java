package de.team33.snippets.e1.runtime;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * Provides user related information from the runtime environment.
 */
public class User {

    /**
     * An absolute, normalized {@link Path} to the user's home directory.
     */
    public static Path HOME = Optional.ofNullable(System.getProperty("user.home"))
                                      .map(Paths::get)
                                      .orElseGet(User::missingHome)
                                      .toAbsolutePath()
                                      .normalize();

    private static Path missingHome() {
        final Path relative = Paths.get("missing", "user", "home");
        return FileSystems.getDefault().getRootDirectories().iterator().next().resolve(relative);
    }

    /**
     * Returns a {@link Path} to an existing directory intended for storing configuration data.
     * The directory name results from the name of a given java package and starts with a period.
     */
    public static Path configPath(final Package p) {
        final Path result = HOME.resolve("." + p.getName());
        try {
            Files.createDirectories(result);
            return result;
        } catch (final IOException e) {
            throw new IllegalStateException(String.format("creation failed: configPath <%s>", result), e);
        }
    }

    public static Path configFile(final Class<?> c) {
        final Package p = c.getPackage();
        final String name = c.getCanonicalName().substring(p.getName().length() + 1);
        return configPath(p).resolve(name);
    }
}
