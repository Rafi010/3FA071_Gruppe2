package dev.hv.test;

import dev.hv.projectFiles.Util;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test_Utils_getRightSystemPath {

    @Test
    public void testWindowsPath() {
        String input = "C:\\Users\\UserName\\Documents";
        String expectedOutput = "C:" + File.separator + "Users" + File.separator + "UserName" + File.separator + "Documents";
        assertEquals(expectedOutput, Util.getRightSystemPath(input));
    }

    @Test
    public void testMixedPath() {
        String input = "C:/Users\\UserName\\Documents";
        String expectedOutput = "C:/Users" + File.separator + "UserName" + File.separator + "Documents";
        assertEquals(expectedOutput, Util.getRightSystemPath(input));
    }

    @Test
    public void testNoBackslashes() {
        String input = "C:/Users/UserName/Documents";
        String expectedOutput = "C:/Users/UserName/Documents"; // Unverändert
        assertEquals(expectedOutput, Util.getRightSystemPath(input));
    }

    @Test
    public void testEmptyPath() {
        String input = "";
        String expectedOutput = "";
        assertEquals(expectedOutput, Util.getRightSystemPath(input));
    }

    @Test
    public void testOnlyBackslashes() {
        String input = "\\\\";
        String expectedOutput = File.separator + File.separator;
        assertEquals(expectedOutput, Util.getRightSystemPath(input));
    }
}