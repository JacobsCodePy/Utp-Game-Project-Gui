package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


/**
 * Allows controlling the board with the keyboard by using GameCursor class.
 */
public class GameKeyboard {

    private final GameCursor cursor;

    public GameKeyboard(JPanel panel, GameController controller, int size) {
        cursor = new GameCursor(controller, size);

        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap =  panel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "enterAction");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "upAction");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "downAction");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "rightAction");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "leftAction");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "undoAction");

        actionMap.put("upAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cursor.moveCursor(-1, 0);
            }
        });
        actionMap.put("downAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cursor.moveCursor(1, 0);
            }
        });
        actionMap.put("leftAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cursor.moveCursor(0, -1);
            }
        });
        actionMap.put("rightAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cursor.moveCursor(0, 1);
            }
        });
        actionMap.put("enterAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GamePosition position = cursor.getSelected().getPosition();
                controller.select(position.row(), position.col());
                cursor.hideCursor();
            }
        });
        actionMap.put("undoAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clear();
            }
        });
    }

    public GameCursor getCursor() {
        return cursor;
    }

    public void setTiles(ArrayList<GameTile> tiles) {
        cursor.setTiles(tiles);
    }

}
