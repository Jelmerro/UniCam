package unicam;

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
 *
 * @author Jelmerro
 */
public class MenuBar extends JMenuBar {

    private SettingsDialog settingsDialog;

    public MenuBar() {
        JMenu menu = new JMenu("Menu");
        add(menu);

        JMenuItem prefs = new JMenuItem("Preferences");
        settingsDialog = new SettingsDialog();
        settingsDialog.refreshWebcams();
        prefs.addActionListener(e -> {
            settingsDialog.refreshWebcams();
            settingsDialog.show();
        });
        prefs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0));
        menu.add(prefs);

        JMenuItem maxres = new JMenuItem("Detect resolution");
        maxres.addActionListener(e -> {
            maxres.setEnabled(false);
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

        JMenuItem screenshot = new JMenuItem("Snapshot");
        screenshot.addActionListener(e -> {
            screenshot.setEnabled(false);
            if (settingsDialog.getCurrentWebcam() != null) {
                try {
                    BufferedImage image = settingsDialog.getCurrentWebcam().getImage();
                    if (image != null) {
                        String date = new Date().toString().replace(":", ".") + ".png";
                        ImageIO.write(image, "png", new File(date));
                        Frame.getInstance().drawMessage("Saved screenshot: " + date);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Failed to save image", "Save failure", JOptionPane.ERROR_MESSAGE);
                }
            }
            enableItem(screenshot);
        });
        screenshot.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0));
        menu.add(screenshot);

        JMenuItem mirror = new JMenuItem("Mirror");
        mirror.addActionListener(e -> {
            mirror.setEnabled(false);
            if (Frame.getInstance().getPanel() != null) {
                Frame.getInstance().getPanel().setMirrored(!Frame.getInstance().getPanel().isMirrored());
            }
            enableItem(mirror);
        });
        mirror.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0));
        menu.add(mirror);

        JMenuItem clipboard = new JMenuItem("Copy to clipboard");
        clipboard.addActionListener(e -> {
            clipboard.setEnabled(false);
            Frame.getInstance().imageToClipboard();
            enableItem(clipboard);
        });
        clipboard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0));
        menu.add(clipboard);

        JMenuItem fullscreen = new JMenuItem("Fullscreen");
        fullscreen.addActionListener(e -> {
            fullscreen.setEnabled(false);
            Frame.getInstance().toggleFullscreen();
            enableItem(fullscreen);
        });
        fullscreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0));
        menu.add(fullscreen);

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> {
            System.exit(0);
        });
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        menu.add(exit);
    }

    public SettingsDialog getSettingsDialog() {
        return settingsDialog;
    }
    
    public void enableItem(JMenuItem item) {
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                item.setEnabled(true);
            }
        }, 300);
    }
}
