package main;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Controls the moving process in the game. Register moves thanks to integration
 * with GameTiles.
 */
public class GameController{

    private GamePosition from;
    private GamePosition to;
    private final GameBoard board;
    private final GameState state;
    private final JFrame frame;

    public GameController(JFrame frame, GameBoard board, GameState state) {
        this.state = state;
        this.frame = frame;
        this.board = board;
    }

    public Stream<GameTile> getTiles() {
        return Arrays.stream(board.getComponents())
                .filter(c -> c instanceof GameTile)
                .map(c -> (GameTile) c);
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

    public void start() {
        getTiles().forEach(tile -> {
            Arrays.stream(tile.getComponents())
                    .filter(component -> component instanceof GamePawn)
                     .forEach(tile::remove);
            tile.setPawn(new GamePawn(board.getAsset(state.get(tile.getPosition()))));
        });
    }

    public void removeTakenPawns(GameMoveResult result) {
        Arrays.stream(result.takenPawns()).forEach(position -> {
            getTiles()
                 .filter(tile -> tile.getPosition().equals(position))
                 .findFirst()
                 .ifPresent(GameTile::unsetPawn);
        });
    }

    public void process(GameMoveResult result) {
        getTiles()
                .filter(tile -> tile.getPosition().equals(from))
                .forEach(GameTile::unsetPawn);
        var toTile = getTiles()
                .filter(tile -> tile.getPosition().equals(to))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Couldn't properly process the move."));
        var queen = state.getCurrentPlayer() != GamePlayerType.White ? // Round has changes this inverted
                GamePawnType.WhiteQueen : GamePawnType.BlackQueen;
        toTile.setPawn(result.isQueen() ? new GamePawn(board.getAsset(queen))
                : new GamePawn(board.getAsset(state.get(toTile.getPosition()))));
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
        handleWinner(result);
        clear();
    }

    public void handleWinner(GameMoveResult result) {
        switch (result.winner()) {
            case White:
                JOptionPane.showMessageDialog(frame, "Congratulations! White has won the game! " +
                        "Now the game will restart.");
                state.reset();
                start();
                break;
            case Black:
                JOptionPane.showMessageDialog(frame, "Congratulations! Black has won the game! " +
                        "Now the game will restart.");
                state.reset();
                start();
                break;
        }
    }

    public void clear() {
        unmark(from.row(), from.col());
        if (to != null) unmark(to.row(), to.col());
        unselect();
    }

    public void mark(int row, int col) {
        getTiles()
                .filter(tile -> tile.getPosition().equals(new GamePosition(row, col)))
                .forEach(GameTile::setMarker);
    }

    public void unmark(int row, int col) {
        getTiles()
                .filter(tile -> tile.getPosition().equals(new GamePosition(row, col)))
                .forEach(GameTile::unsetMarker);
    }

    public boolean isSelected(GamePosition pos) {
        if (from != null) return from.equals(pos);
        else return false;
    }


}
