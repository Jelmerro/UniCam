package unicam;

import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * Dialog displayed when switching panels/settings
 *
 * @author Jelmerro
 */
public class LoadingDialog extends JDialog {

    private JTextArea log;

    /**
     * Constructor of the dialog
     */
    public LoadingDialog() {
        //Call super method
        super(Frame.getInstance(), "Loading", ModalityType.APPLICATION_MODAL);
        //Set properties
        setResizable(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screen.width / 2 - 125, screen.height / 2 - 100);
        setSize(250, 200);
        //Add the log
        log = new JTextArea();
        log.setEditable(false);
        add(log);
    }

    /**
     * Shows the dialog and starts the process of switching panels/settings using the Task class
     *
     * @param webcam Object
     * @param res Dimension
     */
    public void load(Object webcam, Dimension res) {
        Task task = new Task(webcam, res);
        task.start();
        setVisible(true);
    }

    /**
     * Adds the specified string to the displayed log
     *
     * @param line String
     */
    private void addToLog(String line) {
        //Adds it to the textarea and prints it in the console
        log.setText(log.getText() + "\n" + line);
        System.out.println(line);
    }

    /**
     * Inner class Task, responsible for the steps needed to change panels/settings
     */
    private class Task extends Thread {

        Object webcam;
        Dimension res;

        /**
         * Constructor of a Task
         *
         * @param webcam Object
         * @param res Dimension
         */
        public Task(Object webcam, Dimension res) {
            this.webcam = webcam;
            this.res = res;
        }

        /**
         * Thread applying the new panels/settings
         */
        @Override
        public void run() {
            boolean error = false;
            //Stop the existing panel if needed
            if (Frame.getInstance().getPanel() != null) {
                addToLog("Stopping current panel");
                Frame.getInstance().getPanel().stop();
            }
            //Cast the provided object to a webcam
            //If it's null or not a webcam, this step will fail
            addToLog("Setting webcam");
            Webcam selectedWebcam = null;
            try {
                selectedWebcam = (Webcam) webcam;
            } catch (Exception ex) {
                addToLog(ex.toString());
                error = true;
            }
            //Continue only if the webcam is set
            if (selectedWebcam != null) {
                //Try to set the requested resolution
                //This step shouldn't fail under regular circumstances
                addToLog("Setting resolution");
                try {
                    Dimension[] array = {res};
                    selectedWebcam.setCustomViewSizes(array);
                    selectedWebcam.setViewSize(res);
                } catch (Exception ex) {
                    addToLog(ex.toString());
                    error = true;
                }
                //Try to create the new panel
                //If another panel is using the webcam and is frozen, this step will fail
                addToLog("Creating webcam panel");
                try {
                    ViewPanel panel = new ViewPanel(selectedWebcam);
                    addToLog("Building panel");
                    panel.setFPSDisplayed(true);
                    Frame.getInstance().setPanel(panel);
                    panel.updateUI();
                } catch (Exception ex) {
                    addToLog(ex.toString());
                    error = true;
                }
                //If all went well, dispose the LoadingDialog and show a message if the resolution had to be changed
                //Else make the LoadingDialog wider, show the exception and allow closing of the dialog
                if (!error) {
                    dispose();
                    addToLog("Done");
                    if (!res.equals(Frame.getInstance().getPanel().getPreferredSize()) && !(res.getHeight() == 10000 && res.getWidth() == 10000)) {
                        Dimension panelSize = Frame.getInstance().getPanel().getPreferredSize();
                        Frame.getInstance().getPanel().drawMessage("Different resolution: " + (int) panelSize.getWidth() + "x" + (int) panelSize.getHeight() + " instead of " + (int) res.getWidth() + "x" + (int) res.getHeight());
                    }
                } else {
                    setResizable(true);
                    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                    setLocation(screen.width / 2 - 250, screen.height / 2 - 100);
                    setSize(500, 200);
                    addToLog("You can close this window once you're done here");
                    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                }
            } else {
                //Close the LoadingDialog if no webcam was found/selected
                dispose();
                addToLog("No webcam available");
                JOptionPane.showMessageDialog(null, "No usable webcam could be found", "Webcam warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
