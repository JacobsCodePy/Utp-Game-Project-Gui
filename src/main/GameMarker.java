package main;

import javax.swing.*;
import java.awt.*;

/**
 * Covers the marked tile. Used for user interface/experience.
 */
public class GameMarker extends JPanel {

    private final Color color;

    public GameMarker(Color color) {
        this.color = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(15));
        g2d.drawRect(0, 0, getWidth(), getHeight());
    }
}
