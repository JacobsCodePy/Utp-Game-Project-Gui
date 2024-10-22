import javax.swing.*;
import java.util.ArrayList;

/**
 * Controls the moving process in the game. Registers moves thanks to integration
 * with GameTiles.
 */
public class GameController{

    private GamePosition from;
    private GamePosition to;
    private ArrayList<GameTile> tiles;

    public void setTiles(ArrayList<GameTile> tiles) {
        this.tiles = tiles;
    }

    public boolean doesSelectRequirePawn() {
        return from == null;
    }

    public void select(int row, int col) {
        if (from != null) {
            to = new GamePosition(row, col);
            move();
        } else {
            from = new GamePosition(row, col);
        }
    }

    public void unselect() {
        to = null;
        from = null;
    }

    public void move() {
        clear();
        unselect();
    }

    public void clear() {
        for (GameTile tile : tiles) {
            if (tile.getPosition().equals(from) || tile.getPosition().equals(to)) {
                tile.unsetMarker();
            }
        }
    }

    /**
     * Renders marker on screen without any logical checks. With mouse events usually
     * markers are handled by tiles themselves, thus this method is not needed.
     */
    public void moveCursor(int fromRow, int fromCol, int toRow, int toCol) {
        for (GameTile tile : tiles) {
            if (tile.getPosition().equals(new GamePosition(toRow, toCol)))
                tile.forceMarker();
            else if (tile.getPosition().equals(new GamePosition(fromRow, fromCol)))
                tile.unsetMarker();
        }
    }

    /**
     * Triggres marking process on the given tile.
     */
    public void mark(int row, int col) {
        for (GameTile tile : tiles) {
            if (tile.getPosition().equals(new GamePosition(row, col))) {
                tile.forceMarker();
            }
        }
    }

    public boolean isSelected(GamePosition pos) {
        if (from != null) return from.equals(pos);
        else return false;
    }


}
