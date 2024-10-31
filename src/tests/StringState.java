package tests;
import main.GamePawn;
import main.GamePawnType;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Allows to transform String formatted board into the array of pawns given for
 * the C++ module. It makes testing much more readable.
 */
public class StringState {
    private final GamePawnType[] state;

    public StringState(String state) {

        if (!state.trim().matches("((--|Bp|Wp|Bq|Wq)( (--|Bp|Wp|Bq|Wq)){7}[\\r\\n]*){8}")) {
            throw new RuntimeException("State provided as a String is not properly formatted.");
        }
        this.state = Arrays.stream(state.trim().split("\n"))
                .flatMap(line -> Arrays.stream(line.split(" ")))
                .map(this::decodeStringTile)
                .toArray(GamePawnType[]::new);
    }

    public GamePawnType[] getState() {
        return state;
    }

    public GamePawnType decodeStringTile(String tile) {
        return switch (tile) {
            case "Wp" -> GamePawnType.WhitePawn;
            case "Bp" -> GamePawnType.BlackPawn;
            case "Wq" -> GamePawnType.WhiteQueen;
            case "Bq" -> GamePawnType.BlackQueen;
            case "--" -> GamePawnType.Blank;
            default -> throw new RuntimeException("Can't decode string tile - " + tile);
        };
    }

}
