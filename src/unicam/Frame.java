package unicam;

import com.github.sarxos.webcam.WebcamPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 *
 * @author Jelmerro
 */
public class Frame extends JFrame {

    private static Frame frame;
    private static WebcamPanel panel;

    public static Frame getInstance() {
        if (frame == null) {
            frame = new Frame("UniCam");
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            frame.setLayout(new BorderLayout());
            frame.setJMenuBar(new MenuBar());
            frame.pack();
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setSize(new Dimension(screen.width / 2, (int) (screen.height / 1.5)));
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
        frame.add(panel);
        frame.setTitle("UniCam - " + (int) panel.getPreferredSize().getWidth() + "x" + (int) panel.getPreferredSize().getHeight());
    }

    public WebcamPanel getPanel() {
        return panel;
    }
}
