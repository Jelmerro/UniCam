package unicam;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

/**
 * Very simple class that moves your mouse every 30 seconds to prevent sleeping
 *
 * @author Jelmerro
 */
public class AntiSleep extends Thread {

    private Robot antiSleep;

    /**
     * Constructor that creates an awt robot
     *
     * @throws AWTException
     */
    public AntiSleep() throws AWTException {
        setDaemon(true);
        antiSleep = new Robot();

    }

    /**
     * Sleeps for 30 seconds then moves the mouse back and forth
     */
    @Override
    public void run() {
        while (antiSleep != null) {
            //Sleep for 30 seconds
            antiSleep.delay(30000);
            //Move the mouse a bit
            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            antiSleep.mouseMove(mouseLocation.x + 1, mouseLocation.y + 1);
            antiSleep.mouseMove(mouseLocation.x - 1, mouseLocation.y - 1);
            antiSleep.mouseMove(mouseLocation.x, mouseLocation.y);
        }
    }
}
