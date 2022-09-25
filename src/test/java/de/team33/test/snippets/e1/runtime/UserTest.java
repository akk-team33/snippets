package de.team33.test.snippets.e1.runtime;

import de.team33.snippets.e1.runtime.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static final String CONFIG_PATH_SHOULD_EXIST = //
            "after calling <User.configPath()> the resulting path <%s> should exist as a directory";
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
        Files.createFile(Expected.CONFIG_PATH);
        final Path result = User.configPath(CLASS.getPackage());
        assertEquals(Expected.CONFIG_PATH, result);
    }

    @Test
    final void configFile() {
        final Path result = User.configFile(CLASS);
        assertEquals(Expected.OUTER_PATH, result);
    }

    @Test
    final void configFile_SubClass() {
        final Path result = User.configFile(Expected.class);
        assertEquals(Expected.INNER_PATH, result);
    }

    private static class Expected {

        private static final Path HOME = Paths.get(System.getProperty("user.home"));
        private static final Path CONFIG_PATH = HOME.resolve("." + CLASS.getPackage().getName());
        private static final Path OUTER_PATH = CONFIG_PATH.resolve(CLASS.getSimpleName());
        private static final Path INNER_PATH = CONFIG_PATH.resolve(CLASS.getSimpleName()
                                                                           + "."
                                                                           + Expected.class.getSimpleName());
    }
}