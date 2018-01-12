package com.github.jelmerro.unicam;

import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Dialog where you configure the settings
 *
 * @author Jelmerro
 */
public class SettingsDialog extends JDialog {

    private final DefaultComboBoxModel webcamModel;
    private final JComboBox webcamBox;
    private final JTextField XField;
    private final JTextField YField;

    /**
     * Creates the SettingsDialog
     */
    public SettingsDialog() {
        //Call super method
        super(Frame.getInstance(), "Preferences", ModalityType.APPLICATION_MODAL);
        //Set properties
        setResizable(false);
        getRootPane().setBorder(new EmptyBorder(20, 20, 20, 20));
        setBounds(40, 40, 40, 40);
        setLayout(new GridLayout(0, 2, 10, 10));
        setSize(new Dimension(600, 200));
        setPreferredSize(new Dimension(600, 200));
        //Set items
        webcamModel = new DefaultComboBoxModel();
        webcamBox = new JComboBox(webcamModel);
        webcamBox.setMaximumRowCount(10);
        XField = new JTextField("" + 0);
        YField = new JTextField("" + 0);
        webcamBox.addActionListener(e -> {
            Dimension d = getResolution();
            if (d != null) {
                XField.setText("" + d.width);
                YField.setText("" + d.height);
            } else {
                XField.setText("" + 0);
                YField.setText("" + 0);
            }
        });
        JButton doubleButton = new JButton("2X");
        //Doubles the input fields
        doubleButton.addActionListener(e -> {
            try {
                XField.setText("" + (Integer.parseInt(XField.getText()) * 2));
                YField.setText("" + (Integer.parseInt(YField.getText()) * 2));
            } catch (Exception ex) {

            }
        });
        JButton halfButton = new JButton("/2");
        //Halves the input fields
        halfButton.addActionListener(e -> {
            try {
                XField.setText("" + (Integer.parseInt(XField.getText()) / 2));
                YField.setText("" + (Integer.parseInt(YField.getText()) / 2));
            } catch (Exception ex) {

            }
        });
        JButton applyButton = new JButton("Apply");
        //Applies the resolution for the selected webcam
        //This will start the same process as detecting the resolution
        applyButton.addActionListener(e -> {
            setVisible(false);
            //If the input fields contain no only numbers, show a warning and reshow the dialog
            try {
                Dimension dimension = new Dimension(Integer.parseInt(XField.getText()), Integer.parseInt(YField.getText()));
                LoadingDialog loadingDialog = new LoadingDialog();
                loadingDialog.load(webcamBox.getSelectedItem(), dimension);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please only use numbers", "Number warning", JOptionPane.WARNING_MESSAGE);
                setVisible(true);
            }
        });
        JButton cancelButton = new JButton("Cancel");
        //Hides the dialog
        cancelButton.addActionListener(e -> {
            setVisible(false);
        });
        //Add items
        add(new JLabel("Webcam:"));
        add(webcamBox);
        add(new JLabel("Resolution:"));
        Box box = new Box(BoxLayout.X_AXIS);
        box.add(doubleButton);
        box.add(XField);
        box.add(YField);
        box.add(halfButton);
        add(box);
        add(applyButton);
        add(cancelButton);
    }

    /**
     * Refreshes the list of webcams
     */
    public void refreshWebcams() {
        Webcam selectedWebcam = getCurrentWebcam();
        webcamModel.removeAllElements();
        for (Webcam webcam : Webcam.getWebcams()) {
            webcamModel.addElement(webcam);
        }
        if (selectedWebcam != null) {
            webcamModel.setSelectedItem(selectedWebcam);
        }
    }

    /**
     * Returns the Webcam that is currently selected
     *
     * @return webcam Webcam
     */
    public Webcam getCurrentWebcam() {
        return (Webcam) webcamBox.getSelectedItem();
    }

    /**
     * Returns the current resolution, it returns 640x480 when there is no panel
     *
     * @return resolution Dimension
     */
    private Dimension getResolution() {
        if (Frame.getInstance().getPanel() != null) {
            return Frame.getInstance().getPanel().getPreferredSize();
        }
        return new Dimension(640, 480);
    }
}
