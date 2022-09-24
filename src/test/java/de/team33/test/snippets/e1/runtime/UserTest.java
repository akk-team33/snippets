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
    private static final Path HOME = Paths.get(System.getProperty("user.home"));
    private static final Class<?> CLASS = UserTest.class;
    private static final Class<?> SUB_CLASS = UserTest.SubClass.class;
    private static final Path CONFIG_PATH = HOME.resolve("." + CLASS.getPackage().getName());
    private static final Path OUTER_FILE = CONFIG_PATH.resolve(CLASS.getSimpleName());
    private static final Path INNER_FILE = CONFIG_PATH.resolve(CLASS.getSimpleName() + "." + SubClass.class.getSimpleName());

    @BeforeAll
    static void beforeAll() {
        // For manual testing of failing cases ...
        // - - - - - - - - - - - - - - - - - - - -
        // System.getProperties().remove("user.home");
        // System.setProperty("user.home", "/invalid/user/home");
    }

    @BeforeEach
    final void beforeEach() throws IOException {
        Files.deleteIfExists(CONFIG_PATH);
    }

    @AfterEach
    final void afterEach() throws IOException {
        Files.deleteIfExists(CONFIG_PATH);
    }

    @Test
    final void configPath() {
        final Path result = User.configPath(CLASS.getPackage());
        assertEquals(CONFIG_PATH, result);
        assertTrue(Files.isDirectory(result), () -> format(CONFIG_PATH_SHOULD_EXIST, result));
    }

    @Test
    final void configPath_existingFile() throws IOException {
        Files.createFile(CONFIG_PATH);
        assertThrows(IllegalStateException.class, () -> User.configPath(CLASS.getPackage()));
    }

    @Test
    final void configFile() {
        final Path result = User.configFile(CLASS);
        assertEquals(OUTER_FILE, result);
    }

    @Test
    final void configFile_SubClass() {
        final Path result = User.configFile(SUB_CLASS);
        assertEquals(INNER_FILE, result);
    }

    private static class SubClass {
    }
}