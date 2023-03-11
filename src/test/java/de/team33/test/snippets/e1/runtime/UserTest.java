package de.team33.test.snippets.e1.runtime;

import de.team33.snippets.e1.runtime.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    private static final Class<?> CLASS = UserTest.class;

    @BeforeAll
    static void beforeAll() {
        // For manual testing of failing cases ...
        // - - - - - - - - - - - - - - - - - - - -
        // System.getProperties().remove("user.home");
        // System.setProperty("user.home", "/invalid/user/home");
    }

    @BeforeEach
    final void beforeEach() throws IOException {
        Files.deleteIfExists(Expected.CONFIG_PATH);
    }

    @AfterEach
    final void afterEach() throws IOException {
        Files.deleteIfExists(Expected.CONFIG_PATH);
    }

    @Test
    final void home() {
        assertEquals(Expected.HOME, User.home());
    }

    @Test
    final void configPath() {
        final Path result = User.configPath(CLASS.getPackage());
        assertEquals(Expected.CONFIG_PATH, result);
    }

    @Test
    final void configPath_existingDir() throws IOException {
        Files.createDirectories(Expected.CONFIG_PATH);
        final Path result = User.configPath(CLASS.getPackage());
        assertEquals(Expected.CONFIG_PATH, result);
    }

    @Test
    final void configPath_existingFile() throws IOException {
        Files.createDirectories(Expected.CONFIG_PATH.getParent());
        Files.createFile(Expected.CONFIG_PATH);
        final Path result = User.configPath(CLASS.getPackage());
        assertEquals(Expected.CONFIG_PATH, result);
    }

    @ParameterizedTest
    @EnumSource(Extension.class)
    final void configFile(final Extension extension) {
        final Path result = User.configFile(CLASS, extension.input);
        final Path expected = Paths.get(Expected.OUTER_PATH + extension.expected);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @EnumSource(Extension.class)
    final void configFile_SubClass(final Extension extension) {
        final Path result = User.configFile(Expected.class, extension.input);
        final Path expected = Paths.get(Expected.INNER_PATH + extension.expected);
        assertEquals(expected, result);
    }

    @SuppressWarnings("HardcodedLineSeparator")
    enum Extension {
        EXT0(""),
        EXT1("", ""),
        EXT2("", "", "  ", "\t", "\n"),
        EXT3(".json", ".json"),
        EXT4(".cnf", "cnf"),
        EXT5(".abc.def.g.h.i.j.k.l.*", "abc", " ..def", "g h i", " j.k.l ", " * "),
        EXT6("", null, "");

        private final String expected;
        private final String[] input;

        Extension(final String expected, final String... input) {
            this.expected = expected;
            this.input = input;
        }
    }

    private static class Expected {

        private static final Path HOME = Paths.get(System.getProperty("user.home"));
        private static final Path CONFIG_PATH = HOME.resolve(".etc").resolve(CLASS.getPackage().getName());
        private static final Path OUTER_PATH = CONFIG_PATH.resolve(CLASS.getSimpleName());
        private static final Path INNER_PATH = CONFIG_PATH.resolve(CLASS.getSimpleName()
                                                                           + "."
                                                                           + Expected.class.getSimpleName());
    }
}