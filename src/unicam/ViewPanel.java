package unicam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Jelmerro
 */
public class ViewPanel extends WebcamPanel {

    private ArrayList<String> messages;

    public ViewPanel(Webcam webcam) {
        super(webcam);
        messages = new ArrayList<>();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Loops over all messages and draws them
        ArrayList<String> copyOfMessages = new ArrayList<>(messages);
        for (String message : copyOfMessages) {
            //Draws the black shadow
            AttributedString shadowString = new AttributedString(message);
            shadowString.addAttribute(TextAttribute.FOREGROUND, Color.BLACK);
            shadowString.addAttribute(TextAttribute.FONT, new Font("Arial", Font.PLAIN, 12));
            g.drawString(shadowString.getIterator(), 20 + 1, (messages.indexOf(message) + 1) * 20 + 1);
            //Draws the white text
            AttributedString frontString = new AttributedString(message);
            frontString.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);
            frontString.addAttribute(TextAttribute.FONT, new Font("Arial", Font.PLAIN, 12));
            g.drawString(frontString.getIterator(), 20, (messages.indexOf(message) + 1) * 20);
        }
    }

    /**
     * Draws a message on the panel
     *
     * @param message String
     */
    public void drawMessage(String message) {
        //Adds the message to the list
        messages.add(message);
        //Draws the message for 4 seconds
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                //Wait for 4000 milliseconds
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException ex) {

                }
                //Remove the message from the list
                messages.remove(message);
            }
        }, 0);
    }
}
