package unicam;

import com.github.sarxos.webcam.WebcamPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Jelmerro
 */
public class Frame extends JFrame {

    private static Frame frame;
    private static WebcamPanel panel;
    private static boolean fullscreen;
    private static boolean clicked;
    private static long timeClicked;
    private static Date pressedTime;
    private static MenuBar menuBar;
    private static ArrayList<String> messages;

    public static Frame getInstance() {
        if (frame == null) {
            fullscreen = false;
            messages = new ArrayList<>();
            frame = new Frame("UniCam");
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            frame.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    if (clicked) {
                        frame.toggleFullscreen();
                        clicked = false;
                    } else {
                        clicked = true;
                        Timer t = new Timer(true);
                        t.schedule(new TimerTask() {

                            @Override
                            public void run() {
                                clicked = false;
                            }
                        }, 300);
                    }
                    pressedTime = new Date();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    timeClicked = new Date().getTime() - pressedTime.getTime();
                    if (timeClicked > 1000) {
                        Frame.getInstance().imageToClipboard();
                    }
                }
            });
            frame.setLayout(new BorderLayout());
            menuBar = new MenuBar();
            frame.setJMenuBar(menuBar);
            frame.pack();
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setSize(new Dimension(screen.width / 2, (int) (screen.height / 1.5)));
            String imageString = "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";
            frame.setIconImage(frame.Base64ToImage(imageString));
        }
        return frame;
    }

    private Frame(String title) {
        super(title);
    }

    public void setPanel(WebcamPanel p) {
        if (panel != null) {
            frame.remove(panel);
        }
        panel = p;
        add(panel);
        setTitle("UniCam - " + (int) panel.getPreferredSize().getWidth() + "x" + (int) panel.getPreferredSize().getHeight());
    }

    public WebcamPanel getPanel() {
        return panel;
    }

    public void toggleFullscreen() {
        dispose();
        setUndecorated(!fullscreen);
        if (fullscreen) {
            setExtendedState(JFrame.NORMAL);
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            setSize(new Dimension(screen.width / 2, (int) (screen.height / 1.5)));
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        setVisible(true);
        fullscreen = !fullscreen;
    }

    public void imageToClipboard() {
        try {
            BufferedImage bufferedImage = menuBar.getSettingsDialog().getCurrentWebcam().getImage();
            if (bufferedImage != null) {
                TransferableImage image = new TransferableImage(bufferedImage);
                Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                c.setContents(image, null);
                Frame.getInstance().drawMessage("Copied " + new Date().toString().replace(":", ".") + " to clipboard");
            }
        } catch (NullPointerException ex) {
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Image could not be copied", "Clipboard failure", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void drawMessage(String message) {
        AttributedString frontString = new AttributedString(message);
        frontString.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);
        frontString.addAttribute(TextAttribute.FONT, new Font("Arial", Font.PLAIN , 12));
        AttributedString shadowString = new AttributedString(message);
        shadowString.addAttribute(TextAttribute.FOREGROUND, Color.BLACK);
        shadowString.addAttribute(TextAttribute.FONT, new Font("Arial", Font.PLAIN , 12));
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                messages.add(message);
                for (int i = 0; i < 10000; i++) {
                    try {
                        panel.getGraphics().drawString(shadowString.getIterator(), 20, (messages.indexOf(message) + 1) * 20);
                        panel.getGraphics().drawString(frontString.getIterator(), 20 - 1, (messages.indexOf(message) + 1) * 20 - 1);
                    } catch (NullPointerException ex) {

                    }
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    
                }
                messages.remove(message);
            }
        }, 0);
    }

    public Image Base64ToImage(String base64) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64);
            Image image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            return image;
        } catch (IOException ex) {
            return null;
        }
    }
}
