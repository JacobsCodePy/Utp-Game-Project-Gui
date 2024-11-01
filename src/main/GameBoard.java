package main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Displays the board of the game in application. Implements interface functionality.
 */
public class GameBoard extends JPanel {

    private final int size;

    public GameBoard (JFrame frame) {
        super(new GridLayout(8, 8));
        this.size = 8;
        GameState state = new GameState();
        Map<GamePawnType, BufferedImage> assets = new HashMap<>();
        GameController controller = new GameController(frame, state, assets);
        GameKeyboard keyboard = new GameKeyboard(this, controller, size);

        // Adding tiles
        for (int i = 0; i < size * size; i++) {
            int row = i / size;
            int col = i % size;
            this.add(new GameTile(controller, keyboard.getCursor(), new GamePosition(row, col),
                    isTileBlank(i) ?new Color(0xA8, 0x5D, 0x5D)
                            : new Color(0xFF, 0xD2, 0xA6)));
        }

        // Getting tiles and giving them to components, by which they're required
        var tiles = Arrays.stream(getComponents())
                .filter(c -> c instanceof GameTile)
                .map(c -> (GameTile) c).collect(Collectors.toCollection(ArrayList::new));
        controller.setTiles(tiles);
        keyboard.setTiles((tiles));

        // Init pawns images
        assets.put(GamePawnType.BlackPawn,
                GamePawn.loadPawnImage(frame, GamePawnType.BlackPawn));
        assets.put(GamePawnType.WhitePawn,
                GamePawn.loadPawnImage(frame, GamePawnType.WhitePawn));
        assets.put(GamePawnType.BlackQueen,
                GamePawn.loadPawnImage(frame, GamePawnType.BlackQueen));
        assets.put(GamePawnType.WhiteQueen,
                GamePawn.loadPawnImage(frame, GamePawnType.WhiteQueen));
        assets.put(GamePawnType.Blank, null);

        // Starting the game
        controller.start();

    }

    protected boolean isTileBlank(int i) {
        return (i + i / size) % 2 == 0;
    }

}
