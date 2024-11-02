package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

///////////////////////////////
/*
The Checkers game implemented by this code assumes the following rules:
1. Every pawn can move only a single diagonal move forward. Backward direction is forbidden.
2. When pawn captures another pawn it can be in any direction, even backwards.
3. Pawn can capture more than 1 opponent if after taking single pawn it would end up in a situation,
   where it can capture another opponent's pawn without needing to reposition. Such cycle is unlimited
   in the game meaning, if the situation allows to capture e.g. 6 pawns in a single move it is
   allowed. During multi-capturing also direction of the capture does not matter.
4. Queen can move in any diagonal direction for however many places it wants to, unless
   there is a pawn in a way. In such scenario queen is only allowed to jump over the pawn
   if it belongs to the opponent, and it must stop exactly one place after this pawn. Then the
   opponents pawn is capture. Also, such far range capture does not allow for multi-capture chain.
   Only one pawn can be captured in such way during a single move by the queen.
5. When pawn reaches the end of the board it transforms into the queen.
6. The winner is a player, who manages to capture all the opponents pawns.
 */
///////////////////////////////

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

        // Adding GameBoard panel to the app responsively
        GameBoard board = new GameBoard(frame);
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(0xF7, 0xD8,0xB9));
        board.setBackground(new Color(0xF7, 0xD8,0xB9));
        panel.add(board);
        frame.add(panel);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                double width = panel.getWidth();
                double height = panel.getHeight();
                double size = Math.min(width, height);
                double x = (width - size) / 2;
                double y = (height - size) / 2;
                board.setBounds((int)x, (int)y, (int)size, (int)size);
                board.setPreferredSize(new Dimension((int)size, (int)size));
                board.revalidate();
                board.repaint();
            }
        });

        // Prepare and run apps display
        frame.setPreferredSize(new Dimension(800, 800));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}