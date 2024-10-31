package main;

import java.util.ArrayList;

/**
 * Using GameMarkers simulates the cursor for the keyboard interface.
 */
public class GameCursor {

    private GamePosition position;
    private ArrayList<GameTile> tiles;
    private final int size;
    private final GameController controller;

    public GameCursor(GameController controller, int size) {
        this.size = size;
        position = new GamePosition(0, 0);
        this.controller = controller;
    }

    public void setTiles(ArrayList<GameTile> tiles) {
        this.tiles = tiles;
    }

    private boolean isCorrectPosition(GamePosition pos) {
        return (pos.row() >= 0 && pos.row() < size && pos.col() >= 0 && pos.col() < size);
    }

    public GameTile getSelected() {
        return tiles.get(position.getIndex(size));
    }

    public void hideCursor() {
        if (!controller.isSelected(position)) getSelected().unsetMarker();
    }

    public void setCursor(GamePosition position) {
        if (!isCorrectPosition(position)) return;
        hideCursor();
        this.position = position;
        tiles.get(position.getIndex(size)).setMarker();
    }

    public void moveCursor(int by_row, int by_col) {
        setCursor(new GamePosition(position.row() + by_row,
                position.col() + by_col));
    }

}
