package com.github.jelmerro.unicam;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * MenuBar is part of the frame and handles all menu actions
 *
 * @author Jelmerro
 */
public class MenuBar extends JMenuBar {

    private SettingsDialog settingsDialog;
    private AboutDialog aboutDialog;

    /**
     * Creates the MenuBar
     */
    public MenuBar() {
        //Create and add the Menu
        JMenu menu = new JMenu("Menu");
        add(menu);
        //Preferences item
        JMenuItem prefs = new JMenuItem("Preferences");
        //To make sure the detect option works without going to the settings first,
        //already refresh the list of webcams
        settingsDialog = new SettingsDialog();
        settingsDialog.refreshWebcams();
        prefs.addActionListener(e -> {
            //When opening the settings, refresh the list of webcams
            settingsDialog.refreshWebcams();
            settingsDialog.show();
        });
        prefs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0));
        menu.add(prefs);
        //Detect resolution item
        JMenuItem maxres = new JMenuItem("Detect resolution");
        maxres.addActionListener(e -> {
            maxres.setEnabled(false);
            //If a webcam is selected, try to set a resolution of 1000000x1000000
            //The api will automatically lower the resolution to a supported one
            //Also shows the LoadingDialog and displays a warning when no webcam is found
            if (settingsDialog.getCurrentWebcam() != null) {
                Dimension dimension = new Dimension(10000, 10000);
                LoadingDialog loadingDialog = new LoadingDialog();
                loadingDialog.load(settingsDialog.getCurrentWebcam(), dimension);
            } else {
                JOptionPane.showMessageDialog(null, "No usable webcam could be found", "Webcam warning", JOptionPane.WARNING_MESSAGE);
            }
            enableItem(maxres);
        });
        maxres.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0));
        menu.add(maxres);
        //Snapshot item
        JMenuItem screenshot = new JMenuItem("Snapshot");
        screenshot.addActionListener(e -> {
            screenshot.setEnabled(false);
            //If a webcam is selected, try to save the image
            //Any exceptions will display an error message
            if (settingsDialog.getCurrentWebcam() != null) {
                try {
                    BufferedImage image = settingsDialog.getCurrentWebcam().getImage();
                    if (image != null) {
                        String date = new Date().toString().replace(":", ".") + ".png";
                        ImageIO.write(image, "png", new File(date));
                        Frame.getInstance().getPanel().drawMessage("Saved screenshot: " + date);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Failed to save image", "Save failure", JOptionPane.ERROR_MESSAGE);
                }
            }
            enableItem(screenshot);
        });
        screenshot.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0));
        menu.add(screenshot);
        //Mirror item
        JMenuItem mirror = new JMenuItem("Mirror");
        mirror.addActionListener(e -> {
            mirror.setEnabled(false);
            //If exists mirror the panel
            if (Frame.getInstance().getPanel() != null) {
                Frame.getInstance().getPanel().setMirrored(!Frame.getInstance().getPanel().isMirrored());
            }
            enableItem(mirror);
        });
        mirror.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0));
        menu.add(mirror);
        //Copy item
        JMenuItem clipboard = new JMenuItem("Copy to clipboard");
        clipboard.addActionListener(e -> {
            clipboard.setEnabled(false);
            Frame.getInstance().imageToClipboard();
            enableItem(clipboard);
        });
        clipboard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0));
        menu.add(clipboard);
        //Fullscreen item
        JMenuItem fullscreen = new JMenuItem("Fullscreen");
        fullscreen.addActionListener(e -> {
            fullscreen.setEnabled(false);
            Frame.getInstance().toggleFullscreen();
            enableItem(fullscreen);
        });
        fullscreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0));
        menu.add(fullscreen);
        //About item
        JMenuItem about = new JMenuItem("About");
        aboutDialog = new AboutDialog();
        about.addActionListener(e -> {
            aboutDialog.setVisible(true);
        });
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0));
        menu.add(about);
        //Exit item
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> {
            System.exit(0);
        });
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        menu.add(exit);
    }

    /**
     * Getter for the SettingsDialog
     *
     * @return settingsDialog SettingsDialog
     */
    public SettingsDialog getSettingsDialog() {
        return settingsDialog;
    }

    /**
     * Re-enables a disabled item after 300mm to prevent spam
     *
     * @param item JMenuItem
     */
    private void enableItem(JMenuItem item) {
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                item.setEnabled(true);
            }
        }, 300);
    }
}
