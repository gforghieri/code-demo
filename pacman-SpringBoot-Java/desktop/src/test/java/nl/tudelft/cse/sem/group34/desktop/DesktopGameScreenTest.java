package nl.tudelft.cse.sem.group34.desktop;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class DesktopGameScreenTest {
    
    private transient DesktopLauncher launcher;
    
    @Test
    void testLauncherOne() {
        try {
            launcher = new DesktopLauncher();
        } catch (Exception e) {
            fail("Exception thrown whilst trying to construct the application.");
        }
    }
    
    @Test
    void testLauncherTwo() {
        try {
            String[] args = {};
            DesktopLauncher.main(args);
        } catch (Exception e) {
            fail("Exception thrown whilst trying to launch the application.");
        }
    }
    
}
