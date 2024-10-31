package main;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Controls the moving process in the game. Register moves thanks to integration
 * with GameTiles.
 */
public class GameController{

    private GamePosition from;
    private GamePosition to;
    private ArrayList<GameTile> tiles;
    private final GameState state;
    private final JFrame frame;
    private final Map<GamePawnType, BufferedImage> assets;

    public GameController(JFrame frame, GameState state, Map<GamePawnType, BufferedImage> assets) {
        this.state = state;
        this.frame = frame;
        this.assets = assets;
    }

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

    public void removeTakenPawns(GameMoveResult result) {
        Arrays.stream(result.takenPawns()).forEach(position -> {
            tiles.stream()
                 .filter(tile -> tile.getPosition().equals(position))
                 .findFirst()
                 .ifPresent(GameTile::unsetPawn);
        });
    }

    public void process(GameMoveResult result) {
        tiles.stream()
                .filter(tile -> tile.getPosition().equals(from))
                .forEach(GameTile::unsetPawn);
        var toTile = tiles.stream()
                .filter(tile -> tile.getPosition().equals(to))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Couldn't properly process the move."));
        var queen = state.getCurrentPlayer() != GamePlayerType.White ? // Round has changes this inverted
                GamePawnType.WhiteQueen : GamePawnType.BlackQueen;
        toTile.setPawn(result.isQueen() ? new GamePawn(assets.get(queen))
                : new GamePawn(assets.get(state.get(toTile.getPosition()))));
        removeTakenPawns(result);
    }

    public void move() {
        GameMoveResult result = state.process(this.from, this.to);
        if (!result.isCorrect()) {
            JOptionPane.showMessageDialog(frame, result.message());
            clear();
            return;
        }
        process(result);
        clear();
    }

    public void clear() {
        unmark(from.row(), from.col());
        if (to != null) unmark(to.row(), to.col());
        unselect();
    }

    public void mark(int row, int col) {
        tiles.stream()
                .filter(tile -> tile.getPosition().equals(new GamePosition(row, col)))
                .forEach(GameTile::setMarker);
    }

    public void unmark(int row, int col) {
        tiles.stream()
                .filter(tile -> tile.getPosition().equals(new GamePosition(row, col)))
                .forEach(GameTile::unsetMarker);
    }

    public boolean isSelected(GamePosition pos) {
        if (from != null) return from.equals(pos);
        else return false;
    }


}