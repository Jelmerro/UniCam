package com.github.jelmerro.unicam;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.net.URI;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Dialog with info about UniCam
 *
 * @author Jelmerro
 */
public class AboutDialog extends JDialog {

    private final Box box;

    /**
     * Constructor for the dialog
     */
    public AboutDialog() {
        //Call super method
        super(Frame.getInstance(), "About", ModalityType.APPLICATION_MODAL);
        //Set properties
        setResizable(false);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screen.width / 2 - 175, screen.height / 2 - 125);
        setSize(350, 240);
        //Add items
        box = new Box(BoxLayout.Y_AXIS);
        addLabelToBox("UniCam 1.0.0", 20, null);
        addLabelToBox("A cross-platform open-source webcam viewer", 12, null);
        addLabelToBox("Created by Jelmerro", 16, null);
        addLabelToBox("MIT License", 16, null);
        addLabelToBox("Github", 16, "https://github.com/Jelmerro/UniCam");
        add(box);
    }

    /**
     * Creates and adds a label to the box
     *
     * @param text String
     * @param size int
     * @param url String
     */
    private void addLabelToBox(String text, int size, final String url) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("Arial", Font.PLAIN, size));
        label.setBorder(new EmptyBorder(10, 10, 10, 10));
        //If an url is provided, color it, underline it and make it clickable
        if (url != null) {
            label.setForeground(Color.BLUE);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI(url));
                    } catch (Exception ex) {
                        JTextField textField = new JTextField(url);
                        textField.setEditable(false);
                        JOptionPane.showMessageDialog(null, textField, "Website link", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
            Font font = label.getFont();
            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            label.setFont(font.deriveFont(attributes));
        }
        box.add(label);
    }
}
