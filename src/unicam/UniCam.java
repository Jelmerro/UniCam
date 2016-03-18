package unicam;

import java.awt.AWTException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author Jelmerro
 */
public class UniCam {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "SystemLookAndFeel not supported", "UI failure", JOptionPane.ERROR_MESSAGE);
        }
        Frame.getInstance().setVisible(true);
        try {
            AntiSleep antiSleep = new AntiSleep();
            antiSleep.start();
        } catch (AWTException ex) {
            JOptionPane.showMessageDialog(null, "AntiSleep system couldn't start", "Robot failure", JOptionPane.ERROR_MESSAGE);
        }
    }
}
