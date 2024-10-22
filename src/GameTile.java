import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Handles display and input taken by the game cell in the application's GUI.
 */
public class GameTile extends JPanel {

    private final Color color;
    private final GameController controller;
    private final GamePosition position;
    private final GameCursor cursor;
    private boolean hasPawn;
    private boolean isMarked;

    public GameTile(GameController controller, GameCursor cursor,
                    GamePosition position, Color color) {
        super();
        setLayout(new OverlayLayout(this));
        this.controller = controller;
        this.color = color;
        this.position = position;
        this.cursor = cursor;

        // Add mouse click listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                var tile = ((GameTile)e.getComponent());
                tile.setMarker();
                tile.getGameCursor().hideCursor(); // Hide cursor if user uses mouse
            }
        });

        // Background depends on input
        setBackground(color);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void setPawn(GamePawn pawn) {
        this.add(pawn);
        this.hasPawn = true;
    }

    public void unsetPawn() {
        this.remove(0);
        this.hasPawn = false;
    }

    public void setMarker() {
        if (!isMarked && (hasPawn || !controller.doesSelectRequirePawn())) {
            forceMarker();
            controller.select(position.row(), position.col());
            cursor.setCursor(position); // Set cursor to last selected position
        } else if (hasPawn) {
            unsetMarker();
            controller.unselect();
        }
    }

    public void forceMarker() {
        // Sets marker even if it does not fulfill the necessary conditions
        var marker = new GameMarker(Color.GREEN);
        add(marker);
        repaint();
        revalidate();
        isMarked = true;
    }

    public void unsetMarker() {
        for (Component c : getComponents()) {
            if (c instanceof GameMarker) {
                remove(c);
            }
        }
        repaint();
        revalidate();
        isMarked = false;
    }

    public void setAsUnmarked() {
        isMarked = false;
    }

    public GamePosition getPosition() {
        return position;
    }

    public GameCursor getGameCursor() {
        return cursor;
    }
}
