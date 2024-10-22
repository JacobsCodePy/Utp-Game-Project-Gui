import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public Main() {

        // Create the apps window
        JFrame frame = new JFrame("Checkers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creating GameController responsilbe for controlling the game display
        GameController controller = new GameController();

        // Adding GameBoard panel to the app with 8x8 tiles
        GameBoard board = new GameBoard(frame, controller);
        board.start();
        frame.add(board);
        frame.setPreferredSize(new Dimension(800, 800));

        // Prepare and run apps display
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}