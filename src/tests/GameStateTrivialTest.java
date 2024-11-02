package tests;

import main.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Dedicated to testing trivial functionalities of the game. Covers:
 * @1 moving from or to black tiles, which are always forbidden to use.
 * @2 all the possible correct and incorrect moves by white at the start of the game.
 * @3 all the possible correct move of black after white moves
 * @4 moves, which would be correct by black, but they are not due to the white's turn
 * @5 simple scenarios of players winning
 * @6 resetting the game given correct result
 * @7 initialisation of the board from preset state works correctly
 * @8 transformation from the pawn to the queen works correctly
 * @9 moving backwards is incorrect
 * @10 default board initialisation works correctly.
 */
public class GameStateTrivialTest {

    private List<GamePosition> samplePositions;

    @BeforeEach
    public void setUp() {
        samplePositions = List.of(new GamePosition[]{
                new GamePosition(0, 0),
                new GamePosition(4, 5),
                new GamePosition(1, 6),
                new GamePosition(7, 7),
                new GamePosition(5, 2),
        });
    }

    @Test
    public void testIfBoardInitializesCorrectly() {
        var state = new GameState();
        for (int i = 0; i < 64; i++) {
            int row = i / 8;
            int col = i % 8;
            if ((i + i / 8) % 2 == 1) {
                if (i < 24) {
                    Assertions.assertEquals(
                            GamePawnType.BlackPawn, state.get(new GamePosition(row, col))
                    );
                    continue;
                }
                if (i > 63 - 24) {
                    Assertions.assertEquals(
                            GamePawnType.WhitePawn, state.get(new GamePosition(row, col))
                    );
                    continue;
                }
            }
            Assertions.assertEquals(
                    GamePawnType.Blank, state.get(new GamePosition(row, col))
            );
        }
    }

    @Test
    public void testIfInitialisationCorrectlySettingUpTheBoard() {
        var descriptor = new StringState(
                """
                Bq -- -- Wp -- -- Wp --
                -- -- -- -- -- -- -- --
                -- -- -- -- -- -- -- --
                -- -- Bp -- -- -- -- --
                -- -- -- -- Bp -- -- --
                -- -- -- -- -- -- -- --
                -- -- -- -- Wp -- -- --
                -- Wq -- -- -- -- -- Wp
                """
        );
        GamePawnType[] tiles = descriptor.getState();
        GameState state = new GameState(tiles, GamePlayerType.Black);
        Assertions.assertEquals(
                3, state.getBlackPawnsAmount()
        );
        Assertions.assertEquals(
                5, state.getWhitePawnsAmount()
        );
        Assertions.assertEquals(
                GamePlayerType.Black, state.getCurrentPlayer()
        );
        for (int i = 0; i < tiles.length; i++) {
            var row = i / 8;
            var column = i % 8;
            Assertions.assertEquals(
                    tiles[i], state.get(new GamePosition(row, column))
            );
        }
    }

    @TestFactory
    public Collection<DynamicTest> testMovingFromBlackTiles() {
        GameState state = new GameState();
        return IntStream.range(0, 64)
                .mapToObj(i -> new int[]{i / 8, i % 8, (i + i / 8) % 2})
                .filter(arr -> arr[2] == 0)
                .map(arr -> new GamePosition(arr[0], arr[1]))
                .flatMap(fromPos -> samplePositions.stream()
                    .map(toPos -> DynamicTest.dynamicTest(
                            "Move from " + fromPos + " to " + toPos,
                            () -> Assertions.assertFalse(state.process(fromPos, toPos).isCorrect()))
                    ))
                .toList();
    }

    @TestFactory
    public Collection<DynamicTest> testMovingToBlackTiles() {
        GameState state = new GameState();
        return IntStream.range(0, 64)
                .mapToObj(i -> new int[]{i / 8, i % 8, (i + i / 8) % 2})
                .filter(arr -> arr[2] == 0)
                .map(arr -> new GamePosition(arr[0], arr[1]))
                .flatMap(toPos -> samplePositions.stream()
                        .map(fromPos -> DynamicTest.dynamicTest(
                                "Move from " + fromPos + " to " + toPos,
                                () -> Assertions.assertFalse(state.process(fromPos, toPos).isCorrect()))
                        ))
                .toList();
    }

    @TestFactory
    public Collection<DynamicTest> testSingleMoveForwardAtTheBeginningByWhite() {
        GamePosition[] froms = new GamePosition[]{
                new GamePosition(5, 2),
                new GamePosition(5, 4),
                new GamePosition(5, 6),
        };
        GamePosition[] directions = new GamePosition[]{
                new GamePosition(-1, -1),
                new GamePosition(-1, 1),
        };
        return Stream.of(froms).flatMap(from -> Arrays.stream(directions)
                .map((dir) -> {
                    GamePosition to = new GamePosition(from.row() + dir.row(),
                            from.col() + dir.col());
                    return DynamicTest.dynamicTest(
                            "Correct move from " + from + " to " + to,
                            () -> {
                                GameState state = new GameState();
                                Assertions.assertTrue(state.process(from, to).isCorrect());
                            }
                    );
                })).toList();
    }

    @TestFactory
    public Collection<DynamicTest> testSingleMoveForwardByBlackWhenItsWhiteTurn() {
        GamePosition[] froms = new GamePosition[]{
                new GamePosition(2, 1),
                new GamePosition(2, 3),
                new GamePosition(2, 5),
        };
        GamePosition[] directions = new GamePosition[]{
                new GamePosition(1, -1),
                new GamePosition(1, 1),
        };
        return Stream.of(froms).flatMap(from -> Arrays.stream(directions)
                .map((dir) -> {
                    GamePosition to = new GamePosition(from.row() + dir.row(),
                            from.col() + dir.col());
                    return DynamicTest.dynamicTest(
                            "Correct move from " + from + " to " + to,
                            () -> {
                                GameState state = new GameState();
                                Assertions.assertFalse(state.process(from, to).isCorrect());
                            }
                    );
                })).toList();
    }

    @TestFactory
    public Collection<DynamicTest> testForbiddenMovesAtTheBeginningByWhite() {
        GamePosition[] froms = new GamePosition[]{
                new GamePosition(5, 2),
                new GamePosition(5, 4),
                new GamePosition(5, 6),
        };
        GamePosition[] directions = new GamePosition[]{
                new GamePosition(-1, -1),
                new GamePosition(-1, 1),
        };
        var allowed = Stream.of(froms).flatMap(from -> Arrays.stream(directions)
                .map((dir) -> new GamePosition(from.row() + dir.row(),
                            from.col() + dir.col())))
                .toList();
        return IntStream.range(0, 63).mapToObj(i -> new GamePosition(i / 8, i % 8))
                .filter(position -> !allowed.contains(position))
                .flatMap(position -> Stream.of(froms)
                        .map(from -> DynamicTest.dynamicTest(
                                "Incorrect move from " + from + " to " + position,
                                () -> {
                                    GameState state = new GameState();
                                    Assertions.assertFalse(state.process(from, position).isCorrect());
                                }
                        )))
                .toList();
    }

    @TestFactory
    public Collection<DynamicTest> testSingleMoveForwardAtTheBeginningByBlack() {
        GamePosition[] froms = new GamePosition[]{
                new GamePosition(2, 1),
                new GamePosition(2, 3),
                new GamePosition(2, 5),
        };
        GamePosition[] directions = new GamePosition[]{
                new GamePosition(1, -1),
                new GamePosition(1, 1),
        };
        return Stream.of(froms).flatMap(from -> Arrays.stream(directions)
                .map((dir) -> {
                    GamePosition to = new GamePosition(from.row() + dir.row(),
                            from.col() + dir.col());
                    return DynamicTest.dynamicTest(
                            "Correct move from " + from + " to " + to,
                            () -> {
                                GameState state = new GameState();
                                state.process(new GamePosition(5, 2), new GamePosition(4, 3));
                                Assertions.assertTrue(state.process(from, to).isCorrect());
                            }
                    );
                })).toList();
    }

    @Test
    public void testIfTheWinnerIsDetectedCorrectlyAfterCapture() {
        {
            var descriptor = new StringState(
                """
                -- -- -- Wp -- -- Wp --
                -- -- -- -- -- -- -- --
                -- -- -- -- -- -- -- --
                -- -- Bp -- -- -- -- --
                -- Wp -- -- -- -- -- --
                -- -- -- -- -- -- -- --
                -- -- -- -- Wp -- -- --
                -- -- -- -- -- -- -- Wp
                """
            );
            var state = new GameState(descriptor.getState(), GamePlayerType.White);
            GamePosition from = new GamePosition(4, 1);
            GamePosition to = new GamePosition(2, 3);
            Assertions.assertEquals(
                    GamePlayerType.White, state.process(from, to).winner());
        }
        {
            var descriptor = new StringState(
                    """
                    -- -- -- -- -- -- -- --
                    -- -- -- -- -- -- -- --
                    -- -- -- -- -- -- -- --
                    -- -- Bp -- -- -- -- --
                    -- Bp -- Wp -- -- -- --
                    -- -- -- -- -- -- -- --
                    -- -- -- -- -- -- -- --
                    -- -- -- -- -- -- -- --
                    """
            );
            var state = new GameState(descriptor.getState(), GamePlayerType.Black);
            GamePosition from = new GamePosition(3, 2);
            GamePosition to = new GamePosition(5, 4);
            Assertions.assertEquals(
                    GamePlayerType.Black, state.process(from, to).winner());
        }
        {
            var descriptor = new StringState(
                    """
                    -- -- -- -- -- -- -- --
                    -- -- -- -- -- -- -- --
                    -- -- -- -- -- Bp -- --
                    -- -- Bp -- -- -- -- --
                    -- Wp -- -- -- -- -- --
                    -- -- -- -- -- -- -- --
                    -- -- -- Wp -- -- -- --
                    -- -- -- -- -- -- Wp --
                    """
            );
            var state = new GameState(descriptor.getState(), GamePlayerType.White);
            GamePosition from = new GamePosition(4, 1);
            GamePosition to = new GamePosition(2, 3);
            Assertions.assertEquals(
                    GamePlayerType.None, state.process(from, to).winner());
        }
        {
            var descriptor = new StringState(
                    """
                    -- -- -- -- -- -- -- --
                    -- -- -- -- -- -- -- --
                    -- -- -- -- -- Bp -- --
                    -- -- Bp -- -- -- -- --
                    -- Wp -- -- -- -- -- Wp
                    -- -- -- -- -- -- -- --
                    -- -- -- Wp -- -- -- --
                    -- -- -- -- -- -- Wp --
                    """
            );
            var state = new GameState(descriptor.getState(), GamePlayerType.White);
            GamePosition from = new GamePosition(4, 1);
            GamePosition to = new GamePosition(2, 3);
            state.process(from, to);
            from = new GamePosition(2, 5);
            to = new GamePosition(3, 6);
            state.process(from, to);
            from = new GamePosition(4, 7);
            to = new GamePosition(2, 5);
            Assertions.assertEquals(
                    GamePlayerType.White, state.process(from, to).winner());
        }
    }

    @Test
    public void testIfResettingTheGameGivesCorrectBoardState() {
        StringState example = new StringState(
                """
                -- -- -- -- -- -- -- --
                -- -- -- -- -- -- -- --
                -- -- -- -- -- Bp -- --
                -- -- Bp -- -- -- -- --
                -- Wp -- -- -- -- -- Wp
                -- -- -- -- -- -- -- --
                -- -- -- Wp -- -- -- --
                -- -- -- -- -- -- Wp --
                """
        );
        StringState init = new StringState(
                """
                -- Bp -- Bp -- Bp -- Bp
                Bp -- Bp -- Bp -- Bp --
                -- Bp -- Bp -- Bp -- Bp
                -- -- -- -- -- -- -- --
                -- -- -- -- -- -- -- --
                Wp -- Wp -- Wp -- Wp --
                -- Wp -- Wp -- Wp -- Wp
                Wp -- Wp -- Wp -- Wp --
                """
        );
        GameState state = new GameState(example.getState(), GamePlayerType.White);
        state.reset();
        GamePawnType[] tilesInInitialState = init.getState();
        for (int i = 0; i < tilesInInitialState.length; i++) {
            var row = i / 8;
            var column = i % 8;
            Assertions.assertEquals(
                    tilesInInitialState[i], state.get(new GamePosition(row, column))
            );
        }
    }

    @TestFactory
    public Collection<DynamicTest> testIfQueenTransformationOccurred() {
        StringState[] states = new StringState[]{
                new StringState(
                        """
                        -- Bp -- Bp -- -- -- Bp
                        Bp -- Bp -- Bp -- Wp --
                        -- -- -- -- -- -- -- --
                        Wp -- Wp -- -- -- Wp --
                        -- Bp -- -- -- -- -- --
                        Wp -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        """
                ),
                new StringState(
                        """
                        -- -- -- -- -- -- -- Bp
                        -- Bp -- -- Bp -- -- --
                        -- -- -- -- -- -- -- --
                        Wp -- Wp -- -- -- Wp --
                        -- Bp -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- Bp -- -- -- --
                        -- -- -- -- -- -- -- --
                        """
                ),
                new StringState(
                        """
                        -- Bp -- Bp -- -- -- Bp
                        -- -- Bp -- Bp -- Bp --
                        -- -- -- -- -- -- -- Wp
                        Wp -- -- -- -- -- Wp --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        """
                ),
                new StringState(
                        """
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- Bp -- -- -- -- -- --
                        -- -- -- -- -- Bp -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- Bp -- -- -- Wp
                        -- -- Wp -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        """
                )
        };
        GamePosition[] froms = new GamePosition[]{
                new GamePosition(1, 6),
                new GamePosition(6, 3),
                new GamePosition(2, 7),
                new GamePosition(5, 3),
        };
        GamePosition[] tos = new GamePosition[]{
                new GamePosition(0, 5),
                new GamePosition(7, 2),
                new GamePosition(0, 5),
                new GamePosition(7, 1),
        };
        return IntStream.range(0, states.length).mapToObj(i -> DynamicTest.dynamicTest(
                "Test queen transformation (" + i + ")",
                () -> {
                    GameState state = new GameState(states[i].getState(),
                            i % 2 == 0 ? GamePlayerType.White : GamePlayerType.Black);
                    Assertions.assertTrue(state.process(froms[i], tos[i]).isQueen());
                }
        )).toList();
    }

    @Test
    public void testIfMovingBackwardWithoutCaptureIsIncorrect() {
        StringState[] states = new StringState[]{
                new StringState(
                        """
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- Wp -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        """
                ),
                new StringState(
                        """
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- Bp -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        """
                ),
                new StringState(
                        """
                        -- -- -- Wp -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        """
                ),
                new StringState(
                        """
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- -- -- -- --
                        -- -- -- -- Bp -- -- --
                        """
                )
        };
        GamePosition[] froms = new GamePosition[]{
                new GamePosition(4, 3),
                new GamePosition(4, 3),
                new GamePosition(0, 3),
                new GamePosition(7, 4),
        };
        GameMoveResult res;
        GameState.Initializer initializer;
        GameState state;
        for (int i = 0; i < states.length; i++) {
            initializer = new GameState.Initializer(
                    states[i].getState(),
                    i % 2 == 0 ? GamePlayerType.White : GamePlayerType.Black
            );
            state = new GameState(initializer);
            int dir = state.getCurrentPlayer() == GamePlayerType.Black ? -1 : 1;
            res = state.process(froms[i],
                    new GamePosition(froms[i].row() + dir, froms[i].col() + 1));
            Assertions.assertFalse(res.isCorrect());
            state = new GameState(initializer);
            res = state.process(froms[i],
                    new GamePosition(froms[i].row() + dir, froms[i].col() - 1));
            Assertions.assertFalse(res.isCorrect());
        }
    }

}
