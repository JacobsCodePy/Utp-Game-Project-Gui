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

    private final Map<GamePawnType, BufferedImage> assets;

    public GameBoard (JFrame frame) {
        super(new GridLayout(8, 8));
        int size = 8;
        GameState state = new GameState();
        assets = new HashMap<>();
        GameController controller = new GameController(frame, this, state);
        GameKeyboard keyboard = new GameKeyboard(this, controller, size);

        // Adding tiles
        for (int i = 0; i < size * size; i++) {
            int row = i / size;
            int col = i % size;
            this.add(new GameTile(controller, keyboard.getCursor(), new GamePosition(row, col),
                    (i + i / size) % 2 == 0 ? new Color(0xA8, 0x5D, 0x5D)
                            : new Color(0xFF, 0xD2, 0xA6)));
        }

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

    public BufferedImage getAsset (GamePawnType type) {
        return assets.get(type);
    }

}
