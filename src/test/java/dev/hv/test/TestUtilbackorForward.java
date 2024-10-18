package dev.hv.test;

import dev.hv.projectFiles.Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestUtilbackorForward {

    @Test
    void testBackOrForwardOnMac() {
        System.setProperty("os.name", "Mac");
        String result = Util.getRightSystemPath();
        assertEquals("/", result, "Sollte für Mac OS '/' zurückgeben");
    }

    @Test
    void testBackOrForwardOnWindows() {
        System.setProperty("os.name", "Windows");
        String result = Util.getRightSystemPath();
        assertEquals("\\", result, "Sollte für Windows '\\' zurückgeben");
    }

    @Test
    void testBackOrForwardOnOther() {
        System.setProperty("os.name", "Linux");
        String result = Util.getRightSystemPath();
        assertEquals("", result, "Sollte für andere Systeme leeren String zurückgeben");
    }

    // Hilfsmethode, um das Systemproperty zurückzusetzen
    @Test
    public void restoreSystemProperties() {
        System.clearProperty("os.name");
    }
}