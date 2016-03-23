package unicam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JTextArea;

/**
 *
 * @author Jelmerro
 */
public class LoadingDialog extends JDialog {

    JTextArea log;

    public LoadingDialog() {
        super(Frame.getInstance(), ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setFocusableWindowState(false);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screen.width / 2 - 125, screen.height / 2 - 100);
        setSize(250, 200);
        log = new JTextArea();
        log.setEditable(false);
        add(log);
    }

    public void load(Object webcam, Dimension res) {
        Task task = new Task(webcam, res);
        task.start();
        setVisible(true);
    }

    private void addToLog(String line) {
        log.setText(log.getText() + "\n" + line);
        System.out.println(line);
    }

    private class Task extends Thread {

        Object webcam;
        Dimension res;

        public Task(Object webcam, Dimension res) {
            this.webcam = webcam;
            this.res = res;
        }

        @Override
        public void run() {
            boolean error = false;
            if (Frame.getInstance().getPanel() != null) {
                addToLog("Stopping current panel");
                Frame.getInstance().getPanel().stop();
            }
            addToLog("Setting webcam");
            Webcam selectedWebcam = null;
            try {
                selectedWebcam = (Webcam) webcam;
            } catch (Exception ex) {
                addToLog(ex.toString());
                error = true;
            }
            addToLog("Setting resolution");
            try {
                Dimension[] array = {res};
                selectedWebcam.setCustomViewSizes(array);
                selectedWebcam.setViewSize(res);
            } catch (Exception ex) {
                addToLog(ex.toString());
                error = true;
            }
            addToLog("Creating webcam panel");
            try {
                WebcamPanel panel = new WebcamPanel(selectedWebcam);
                addToLog("Building panel");
                panel.setFPSDisplayed(true);
                Frame.getInstance().setPanel(panel);
                panel.updateUI();
            } catch (Exception ex) {
                addToLog(ex.toString());
                error = true;
            }
            if (!error) {
                dispose();
                addToLog("Done");
                if (!res.equals(Frame.getInstance().getPanel().getPreferredSize()) && !(res.getHeight() == 10000 && res.getWidth() == 10000)) {
                    Dimension panelSize = Frame.getInstance().getPanel().getPreferredSize();
                    Frame.getInstance().drawMessage("Different resolution: " + (int) panelSize.getWidth() + "x" + (int) panelSize.getHeight() + " instead of " + (int) res.getWidth() + "x" + (int) res.getHeight());
                }
            } else {
                addToLog("You can close this window once you're done here");
                setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                setFocusableWindowState(true);
            }
        }
    }
}
