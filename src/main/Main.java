package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {

    public Main() {

        // Create the apps window
        JFrame frame = new JFrame("Checkers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Try to set look and feel according to the OS
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Os look and feel not supported.");
        }

        // Loading icon if possible
        try {
            var icon = ImageIO.read(new File("resource/icon.png"));
            frame.setIconImage(icon);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "Couldn't find the applications icon.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Couldn't load the applications icon.");
        }

        // Adding GameBoard panel to the app with 8x8 tiles
        GameBoard board = new GameBoard(frame);
        board.setPreferredSize(new Dimension(800, 800));
        frame.add(board);

        // Prepare and run apps display
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}