package unicam;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

/**
 *
 * @author Jelmerro
 */
public class AntiSleep extends Thread {

    Robot antiSleep;

    public AntiSleep() throws AWTException {
        setDaemon(true);
        antiSleep = new Robot();

    }

    @Override
    public void run() {
        while (antiSleep != null) {
            antiSleep.delay(30000);
            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            antiSleep.mouseMove(mouseLocation.x + 1, mouseLocation.y + 1);
            antiSleep.mouseMove(mouseLocation.x - 1, mouseLocation.y - 1);
            antiSleep.mouseMove(mouseLocation.x, mouseLocation.y);
        }
    }
}
