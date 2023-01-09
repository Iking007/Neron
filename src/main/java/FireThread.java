import java.awt.*;
import java.awt.event.InputEvent;

/**
 * @author Pavel
 */
public class FireThread implements Runnable{
    @Override
    public void run() {
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        assert robot != null;
        robot.mousePress(InputEvent.BUTTON1_MASK);
        try {
            Main.threadScreen.key = false;
            Thread.sleep(500);
            Main.threadScreen.key = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}
