package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

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
                tile.mark();
            }
        });

        // Background depends on input
        setBackground(color);
    }

    public void mark() {
        if (isMarked) {
            unsetMarker();
            controller.unselect();
            isMarked = false;
        } else {
            setMarker();
            controller.select(position.row(), position.col());
            isMarked = true;
        }
        cursor.setCursor(position);
        cursor.hideCursor();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void setMarker() {
        var marker = new GameMarker(new Color(0x55, 0xB2, 0xFF));
        add(marker);
        repaint();
        revalidate();
        isMarked = true;
    }

    public void unsetMarker() {
        Arrays.stream(getComponents())
                .filter(c -> c instanceof GameMarker)
                .forEach(this::remove);
        repaint();
        revalidate();
        isMarked = false;
    }

    public void setPawn(GamePawn pawn) {
        if (!pawn.isBlank()) {
            this.add(pawn);
            this.hasPawn = true;
        }
    }

    public void unsetPawn() {
        Arrays.stream(getComponents())
                .filter(c -> c instanceof GamePawn)
                .forEach(this::remove);
        repaint();
        revalidate();
        this.hasPawn = false;
    }

    public GameCursor getGameCursor() {
        return cursor;
    }

    public GamePosition getPosition() {
        return position;
    }
}
