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

    public static BufferedImage loadPawnImage(JFrame frame, Type type) {
        return switch(type) {
            case NormalWhite -> loadImage(frame, "assets/white.png");
            case NormalBlack -> loadImage(frame, "assets/black.png");
            case QueenWhite -> loadImage(frame, "assets/white-queen.png");
            case QueenBlack -> loadImage(frame, "assets/black-queen.png");
        };
    }

    public enum Type {
        NormalWhite, NormalBlack, QueenWhite, QueenBlack,
    }
}
