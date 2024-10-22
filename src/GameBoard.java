import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Displays the board of the game in application. Implements interface functionality.
 */
public class GameBoard extends JPanel {
    private final GameController controller;
    private final GameCursor cursor;
    private final int size;
    private final Map<GamePawn.Type, BufferedImage> pawns;

    protected boolean isTileBlank(int i) {
        return (i + i / size) % 2 == 0;
    }

    public GameBoard (JFrame frame, GameController controller) {
        super(new GridLayout(8, 8));
        this.controller = controller;
        this.size = 8;
        this.cursor = new GameCursor(controller, size);

        // Adding tiles
        for (int i = 0; i < size * size; i++) {
            int row = i / size;
            int col = i % size;
            this.add(new GameTile(controller, cursor, new GamePosition(row, col),
                    isTileBlank(i) ? Color.WHITE : Color.BLACK));
        }

        // Getting tiles and giving them to components, by which they're required
        var tiles = Arrays.stream(getComponents())
                .filter(c -> c instanceof GameTile)
                .map(c -> (GameTile) c).collect(Collectors.toCollection(ArrayList::new));
        controller.setTiles(tiles);
        cursor.setTiles(tiles);

        // Enabling keyboard controller
        allowKeyboardController();

        // Init pawns images
        pawns = new HashMap<>();
        pawns.put(GamePawn.Type.NormalBlack,
                GamePawn.loadPawnImage(frame, GamePawn.Type.NormalBlack));
        pawns.put(GamePawn.Type.NormalWhite,
                GamePawn.loadPawnImage(frame, GamePawn.Type.NormalWhite));
        pawns.put(GamePawn.Type.QueenBlack,
                GamePawn.loadPawnImage(frame, GamePawn.Type.QueenBlack));
        pawns.put(GamePawn.Type.QueenWhite,
                GamePawn.loadPawnImage(frame, GamePawn.Type.QueenWhite));

    }

    public void start() {
        // Amount of total pawns of one side
        int maxPawns = 12;
        int boardSize = size * size;
        IntStream.range(0, boardSize)
                .filter(this::isTileBlank) // Get proper indices of a board
                .limit(maxPawns) // Get porper
                .mapToObj(Arrays.asList(getComponents())::get) // Take indices from components
                .filter(component -> component instanceof GameTile)
                .map(component -> (GameTile) component)
                .forEach(tile -> tile.setPawn(new GamePawn(pawns.get(GamePawn.Type.NormalWhite))));
        IntStream.range(0, boardSize)
                .map(i -> boardSize - i - 1) // Inverse indices
                .filter(this::isTileBlank) // Get proper indices of a board
                .limit(maxPawns) // Get porper
                .mapToObj(Arrays.asList(getComponents())::get) // Take indices from components
                .filter(component -> component instanceof GameTile)
                .map(component -> (GameTile) component)
                .forEach(tile -> tile.setPawn(new GamePawn(pawns.get(GamePawn.Type.NormalBlack))));

    }

    public void allowKeyboardController() {
        // Map arrow keys and Enter to actions
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap =  getActionMap();

        // Bind the "UP" arrow key
        inputMap.put(KeyStroke.getKeyStroke("UP"), "upAction");
        actionMap.put("upAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cursor.moveCursor(-1, 0);
            }
        });

        // Bind the "DOWN" arrow key
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "downAction");
        actionMap.put("downAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cursor.moveCursor(1, 0);
            }
        });

        // Bind the "LEFT" arrow key
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "leftAction");
        actionMap.put("leftAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cursor.moveCursor(0, -1);
            }
        });

        // Bind the "RIGHT" arrow key
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "rightAction");
        actionMap.put("rightAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cursor.moveCursor(0, 1);
            }
        });

        // Bind the "Enter" key
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "enterAction");
        actionMap.put("enterAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cursor.getSelected().setAsUnmarked();
                cursor.getSelected().setMarker();
            }
        });
    }

}
