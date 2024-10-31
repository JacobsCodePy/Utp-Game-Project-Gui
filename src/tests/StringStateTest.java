package tests;

import main.GamePawnType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StringStateTest {

    private StringState state;

    @BeforeEach
    public void setUp() {
        state = new StringState(
            """
            -- Bp -- -- -- -- -- Bp
            Bp -- -- -- Bp -- Bp --
            -- -- -- -- -- -- -- --
            Bp -- Wp -- Bp -- -- --
            -- -- -- Wp -- -- -- --
            -- -- -- -- -- -- Wp --
            -- -- -- -- -- Bp -- Wp
            Wp -- Wp -- -- -- -- --
            """
        );
    }

    @Test
    public void testCharacterDecoding() {
        Assertions.assertEquals(
                GamePawnType.WhiteQueen, state.decodeStringTile("Wq")
        );
        Assertions.assertEquals(
                GamePawnType.BlackQueen, state.decodeStringTile("Bq")
        );
        Assertions.assertEquals(
                GamePawnType.WhitePawn, state.decodeStringTile("Wp")
        );
        Assertions.assertEquals(
                GamePawnType.BlackPawn, state.decodeStringTile("Bp")
        );
        Assertions.assertEquals(
                GamePawnType.Blank, state.decodeStringTile("--")
        );
        Assertions.assertThrows(RuntimeException.class, () -> {
            state.decodeStringTile(" ");
        });
        Assertions.assertThrows(RuntimeException.class, () -> {
            state.decodeStringTile("B");
        });
        Assertions.assertThrows(RuntimeException.class, () -> {
            state.decodeStringTile("W");
        });
        Assertions.assertThrows(RuntimeException.class, () -> {
            state.decodeStringTile("q");
        });
    }

    @Test
    public void testDecodedStateCorrectness() {
        var expected = new GamePawnType[64];
        for (int i = 0; i < expected.length; i++) {
            int row = i / 8;
            int col = i % 8;
            expected[i] =GamePawnType.Blank;
            if (row == 0 && col == 1) expected[i] = GamePawnType.BlackPawn;
            if (row == 0 && col == 7) expected[i] = GamePawnType.BlackPawn;
            if (row == 1 && col == 0) expected[i] = GamePawnType.BlackPawn;
            if (row == 1 && col == 4) expected[i] = GamePawnType.BlackPawn;
            if (row == 1 && col == 6) expected[i] = GamePawnType.BlackPawn;
            if (row == 3 && col == 0) expected[i] = GamePawnType.BlackPawn;
            if (row == 3 && col == 2) expected[i] = GamePawnType.WhitePawn;
            if (row == 3 && col == 4) expected[i] = GamePawnType.BlackPawn;
            if (row == 4 && col == 3) expected[i] = GamePawnType.WhitePawn;
            if (row == 5 && col == 6) expected[i] = GamePawnType.WhitePawn;
            if (row == 6 && col == 5) expected[i] = GamePawnType.BlackPawn;
            if (row == 6 && col == 7) expected[i] = GamePawnType.WhitePawn;
            if (row == 7 && col == 0) expected[i] = GamePawnType.WhitePawn;
            if (row == 7 && col == 2) expected[i] = GamePawnType.WhitePawn;
        }
        var decoded = state.getState();
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertEquals(
                    expected[i], decoded[i]
            );
        }
    }

}
