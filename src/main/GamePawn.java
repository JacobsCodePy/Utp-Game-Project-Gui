package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Can display image of a given type of game Pawn.
 */
public class GamePawn extends JPanel {
    private final BufferedImage image;

    public GamePawn(BufferedImage image) {
        this.image = image;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this.getSize().width,
                this.getSize().height, null);
    }

    private static BufferedImage loadImage(JFrame frame, String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Couldn't load the game " +
                    "pawn picture (" + path + ").");
            return null;
        }
    }

    public static BufferedImage loadPawnImage(JFrame frame, GamePawnType type) {
        return switch(type) {
            case WhitePawn -> loadImage(frame, "resource/white.png");
            case BlackPawn -> loadImage(frame, "resource/black.png");
            case WhiteQueen -> loadImage(frame, "resource/white-queen.png");
            case BlackQueen -> loadImage(frame, "resource/black-queen.png");
            case Blank -> null;
        };
    }

    public boolean isBlank () {
        return this.image == null;
    }

}
