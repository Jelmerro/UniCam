package unicam;

import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Jelmerro
 */
public class SettingsPopup extends JPanel {

    private final DefaultComboBoxModel webcamModel;
    private final JComboBox webcamBox;
    private final JTextField XField;
    private final JTextField YField;

    public SettingsPopup() {
        webcamModel = new DefaultComboBoxModel();
        webcamBox = new JComboBox(webcamModel);
        Box resBox = new Box(BoxLayout.X_AXIS);
        XField = new JTextField();
        YField = new JTextField();
        webcamBox.addActionListener(e -> {
            Dimension d = getResolution();
            if (d != null) {
                XField.setText("" + d.width);
                YField.setText("" + d.height);
            }
        });
        resBox.add(XField);
        resBox.add(YField);
        add(new JLabel("Webcam:"));
        add(webcamBox);
        add(new JLabel("Resolution:"));
        add(resBox);
        setLayout(new GridLayout(2, 2, 20, 20));
    }

    @Override
    public void show() {
        boolean loop = true;
        while (loop) {
            loop = false;
            int result = JOptionPane.showConfirmDialog(null, this, "UniCam", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    Dimension dimension = new Dimension(Integer.parseInt(XField.getText()), Integer.parseInt(YField.getText()));
                    LoadingDialog loadingDialog = new LoadingDialog();
                    loadingDialog.load(webcamBox.getSelectedItem(), dimension);
                } catch (NumberFormatException ex) {
                    loop = true;
                }
            }
        }
    }

    public void refreshWebcams() {
        webcamModel.removeAllElements();
        for (Webcam webcam : Webcam.getWebcams()) {
            webcamModel.addElement(webcam);
        }
    }

    public Webcam getCurrentWebcam() {
        return (Webcam) webcamBox.getSelectedItem();
    }

    private Dimension getResolution() {
        Webcam tempCam = (Webcam) webcamBox.getSelectedItem();
        if (tempCam != null) {
            return tempCam.getViewSizes()[tempCam.getViewSizes().length - 1];
        }
        return null;
    }
}
