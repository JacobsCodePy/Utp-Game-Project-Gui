package tests;

import main.GameMoveResult;
import main.GamePlayerType;
import main.GamePosition;
import main.GameState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Dedicated to testing individual scenarios in terms of capturing correct amount of pawns
 * in correct places and correctness of the move. It uses special class, which not only
 * tests if the specified correct moves given correct amount and positions of captured pawns
 * but also if all the other moves are recognized properly as incorrect ones.
 */
public class GameStateCaptureTest {

    /**
     * In order to quickly check scenarios this computes all the moves from given position
     * and checks whether only the ones specified as correct are validated whereas other should
     * be marked as incorrect. The correct moves are also investigated in terms of correct capturing
     * of the opponents pawns if such occurred.
     */
    private static class ScenarioTester {

        private final StringState state;
        private final GamePlayerType player;
        private final Map<GamePosition, List<GamePosition>> moveMap;
        private GamePosition startingPosition;

        public ScenarioTester(StringState state, GamePlayerType plauer) {
            this.state = state;
            this.player = plauer;
            this.moveMap = new HashMap<>();
        }

        public void addStart(GamePosition startingPosition) {
            this.startingPosition = startingPosition;
        }

        public void addVariant(GamePosition correctPosition, GamePosition[] pawnsCaptured) {
            this.moveMap.put(correctPosition, Arrays.stream(pawnsCaptured).toList());
        }

        public Collection<DynamicTest> test() {
            return IntStream.range(0, 63).mapToObj(i -> new GamePosition(i / 8, i % 8))
                    .map(position -> DynamicTest.dynamicTest(
                            moveMap.containsKey(position) ? "Correct move to " + position
                                    : "Incorrect move to " + position,
                            () -> {
                                GameState st = new GameState(state.getState(), player);
                                GameMoveResult result = st.process(startingPosition, position);
                                System.out.println(result.message());
                                if (moveMap.containsKey(position)) {
                                    Assertions.assertTrue(result.isCorrect());
                                    Assertions.assertEquals(
                                            moveMap.get(position).size(),
                                            result.takenPawns().length
                                    );
                                    Iterator<GamePosition> iterator = moveMap.get(position).iterator();
                                    Arrays.stream(result.takenPawns()).forEach(pawn ->
                                            Assertions.assertEquals(
                                                    iterator.next(), pawn
                                            )
                                    );
                                } else Assertions.assertFalse(result.isCorrect());
                            }
                    )).toList();
        }
    }

    @TestFactory
    public Collection<DynamicTest> testScenarioOfSinglePawnCaptureWhite1() {
        StringState state = new StringState(
                """
                -- Bp -- Bp -- Bq -- Bp
                Bp -- Bp -- Bp -- Bp --
                -- -- -- Bp -- -- -- --
                -- -- Bp -- Bp -- Bp --
                -- -- -- Wp -- -- -- Wp
                Wp -- -- -- Wp -- -- --
                -- Wq -- -- -- Wq -- Wp
                Wp -- Wp -- Wp -- Wp --
                """
        );
        ScenarioTester tester = new ScenarioTester(state, GamePlayerType.White);
        tester.addStart(new GamePosition(4, 3));
        tester.addVariant(new GamePosition(2, 1), new GamePosition[]{
                new GamePosition(3, 2),
        });
        tester.addVariant(new GamePosition(2, 5), new GamePosition[]{
                new GamePosition(3, 4),
        });
        return tester.test();
    }

    @TestFactory
    public Collection<DynamicTest> testScenarioOfSinglePawnCaptureWhite2() {
        StringState state = new StringState(
                """
                -- Bp -- Bp -- Bp -- Bp
                Bp -- -- -- -- -- Bp --
                -- -- -- -- -- -- -- --
                -- -- -- -- Bp -- Bp --
                -- -- -- Bp -- Wp -- Wp
                Wp -- -- -- Bq -- Bp --
                -- Wp -- -- -- Wp -- --
                Wq -- Wp -- Wp -- Wp --
                """
        );
        ScenarioTester tester = new ScenarioTester(state, GamePlayerType.White);
        tester.addStart(new GamePosition(4, 5));
        tester.addVariant(new GamePosition(2, 7), new GamePosition[]{
                new GamePosition(3, 6),
        });
        tester.addVariant(new GamePosition(6, 3), new GamePosition[]{
                new GamePosition(5, 4),
        });
        tester.addVariant(new GamePosition(6, 7), new GamePosition[]{
                new GamePosition(5, 6),
        });
        tester.addVariant(new GamePosition(2, 3), new GamePosition[]{
                new GamePosition(3, 4),
        });
        return tester.test();
    }

    @TestFactory
    public Collection<DynamicTest> testScenarioOfSinglePawnCaptureWhite3() {
        StringState state = new StringState(
                """
                -- -- -- Bp -- Bq -- Bp
                Bp -- -- -- Wp -- -- --
                -- -- -- Bq -- -- -- --
                -- -- -- -- -- -- -- --
                -- -- -- Bp -- -- -- Wp
                Wp -- -- -- Bp -- -- --
                -- Wp -- -- -- -- -- Wp
                Wp -- -- -- Wp -- Wp --
                """
        );
        ScenarioTester tester = new ScenarioTester(state, GamePlayerType.White);
        tester.addStart(new GamePosition(1, 4));
        tester.addVariant(new GamePosition(3, 2), new GamePosition[]{
                new GamePosition(2, 3),
        });
        return tester.test();
    }

    @TestFactory
    public Collection<DynamicTest> testScenarioOfSinglePawnCaptureBlack1() {
        StringState state = new StringState(
                """
                -- Bp -- Bp -- Bp -- Bp
                Bp -- Bp -- Bp -- Bp --
                -- -- -- -- -- -- -- --
                -- -- Bp -- -- -- Bp --
                -- Bp -- Wp -- -- -- Wp
                Wp -- Wp -- -- -- -- --
                -- Wp -- -- -- Wp -- Wp
                Wp -- Wp -- Wq -- Wp --
                """
        );
        ScenarioTester tester = new ScenarioTester(state, GamePlayerType.Black);
        tester.addStart(new GamePosition(4, 1));
        tester.addVariant(new GamePosition(6, 3), new GamePosition[]{
                new GamePosition(5, 2),
        });
        return tester.test();
    }

    @TestFactory
    public Collection<DynamicTest> testScenarioOfSinglePawnCaptureBlack2() {
        StringState state = new StringState(
                """
                -- Bp -- Bp -- Bp -- Bp
                Bp -- -- -- Bp -- -- --
                -- -- -- Wq -- Wp -- --
                -- -- Bp -- Bp -- Bp --
                -- Bp -- Wp -- Bp -- Wp
                -- -- -- -- -- -- -- --
                -- Wq -- -- -- Wp -- Wp
                Wp -- Wp -- Wp -- Wp --
                """
        );
        ScenarioTester tester = new ScenarioTester(state, GamePlayerType.Black);
        tester.addStart(new GamePosition(3, 4));
        tester.addVariant(new GamePosition(1, 6), new GamePosition[]{
                new GamePosition(2, 5),
        });
        tester.addVariant(new GamePosition(1, 2), new GamePosition[]{
                new GamePosition(2, 3),
        });
        tester.addVariant(new GamePosition(5, 2), new GamePosition[]{
                new GamePosition(4, 3),
        });
        return tester.test();
    }

    @TestFactory
    public Collection<DynamicTest> testScenarioOfSinglePawnCaptureBlack3() {
        StringState state = new StringState(
                """
                -- Bp -- Bp -- Bp -- Bp
                -- -- -- -- -- -- -- --
                -- -- -- -- -- -- -- --
                -- -- Bp -- -- -- -- --
                -- Wp -- -- -- -- -- Wp
                -- -- Wp -- Wp -- -- --
                -- Wp -- Bp -- Wp -- Wp
                Wp -- -- -- -- -- -- --
                """
        );
        ScenarioTester tester = new ScenarioTester(state, GamePlayerType.Black);
        tester.addStart(new GamePosition(6, 3));
        tester.addVariant(new GamePosition(4, 5), new GamePosition[]{
                new GamePosition(5, 4),
        });
        tester.addVariant(new GamePosition(7, 2), new GamePosition[0]);
        tester.addVariant(new GamePosition(7, 4), new GamePosition[0]);
        return tester.test();
    }

    @TestFactory
    public Collection<DynamicTest> testScenarioOfMultiplePawnCaptureWhite1() {
        StringState state = new StringState(
                """
                -- Bp -- -- -- Bp -- Bp
                -- -- Bp -- Bp -- -- --
                -- -- -- -- -- -- -- --
                Wp -- -- -- Bq -- -- --
                -- -- -- Wp -- -- -- Wp
                -- -- Wq -- Bp -- -- --
                -- -- -- Bp -- -- -- Wp
                Wp -- Wp -- Wp -- -- --
                """
        );
        ScenarioTester tester = new ScenarioTester(state, GamePlayerType.White);
        tester.addStart(new GamePosition(4, 3));
        tester.addVariant(new GamePosition(2, 5), new GamePosition[]{
                new GamePosition(3, 4),
        });
        tester.addVariant(new GamePosition(0, 3), new GamePosition[]{
                new GamePosition(3, 4),
                new GamePosition(1, 4),
        });
        tester.addVariant(new GamePosition(2, 1), new GamePosition[]{
                new GamePosition(3, 4),
                new GamePosition(1, 4),
                new GamePosition(1, 2),
        });
        tester.addVariant(new GamePosition(6, 5), new GamePosition[]{
                new GamePosition(5, 4),
        });
        tester.addVariant(new GamePosition(3, 2), new GamePosition[0]);
        return tester.test();
    }

    @TestFactory
    public Collection<DynamicTest> testScenarioOfMultiplePawnCaptureWhite2() {
        StringState state = new StringState(
                """
                -- Bp -- -- -- Bp -- Bp
                -- -- Wp -- Wp -- -- --
                -- -- -- Bp -- Bp -- --
                -- -- -- -- -- -- -- --
                -- Wp -- Wp -- Bp -- Wp
                -- -- -- -- -- -- -- --
                -- -- -- Bp -- Bp -- Bp
                Wp -- -- -- -- -- -- --
                """
        );
        ScenarioTester tester = new ScenarioTester(state, GamePlayerType.White);
        tester.addStart(new GamePosition(1, 2));
        tester.addVariant(new GamePosition(3, 4), new GamePosition[]{
                new GamePosition(2, 3),
        });
        tester.addVariant(new GamePosition(5, 6), new GamePosition[]{
                new GamePosition(2, 3),
                new GamePosition(4, 5),
        });
        tester.addVariant(new GamePosition(1, 6), new GamePosition[]{
                new GamePosition(2, 3),
                new GamePosition(2, 5),
        });
        tester.addVariant(new GamePosition(7, 4), new GamePosition[]{
                new GamePosition(2, 3),
                new GamePosition(4, 5),
                new GamePosition(6, 5),
        });
        tester.addVariant(new GamePosition(5, 2), new GamePosition[]{
                new GamePosition(2, 3),
                new GamePosition(4, 5),
                new GamePosition(6, 5),
                new GamePosition(6, 3),
        });
        tester.addVariant(new GamePosition(0, 3), new GamePosition[0]);
        return tester.test();
    }

    @TestFactory
    public Collection<DynamicTest> testScenarioOfMultiplePawnCaptureBlack1() {
        StringState state = new StringState(
                """
                -- Bp -- -- -- Bp -- --
                -- -- Wp -- Bp -- Wp --
                -- -- -- -- -- Bp -- --
                Wp -- Wp -- Wp -- -- --
                -- -- -- -- -- Wp -- --
                -- -- Wp -- Bp -- -- --
                -- Bp -- Bp -- -- -- Wp
                Wp -- Wp -- Wq -- -- --
                """
        );
        ScenarioTester tester = new ScenarioTester(state, GamePlayerType.Black);
        tester.addStart(new GamePosition(2, 5));
        tester.addVariant(new GamePosition(0, 7), new GamePosition[]{
                new GamePosition(1, 6),
        });
        tester.addVariant(new GamePosition(4, 3), new GamePosition[]{
                new GamePosition(3, 4),
        });
        tester.addVariant(new GamePosition(2, 1), new GamePosition[]{
                new GamePosition(3, 4),
                new GamePosition(3, 2),
        });
        tester.addVariant(new GamePosition(0, 3), new GamePosition[]{
                new GamePosition(3, 4),
                new GamePosition(3, 2),
                new GamePosition(1, 2),
        });
        tester.addVariant(new GamePosition(3, 6), new GamePosition[0]);
        return tester.test();
    }

    @TestFactory
    public Collection<DynamicTest> testScenarioOfMultiplePawnCaptureBlack2() {
        StringState state = new StringState(
                """
                -- Bp -- -- -- -- -- --
                Bp -- Wp -- Bp -- -- --
                -- -- -- -- -- -- -- --
                Wp -- Bp -- Wq -- Bp --
                -- -- -- -- -- -- -- --
                -- -- Wp -- Bp -- -- --
                -- -- -- Bp -- -- -- Wp
                -- -- -- -- -- -- -- --
                """
        );
        ScenarioTester tester = new ScenarioTester(state, GamePlayerType.Black);
        tester.addStart(new GamePosition(0, 1));
        tester.addVariant(new GamePosition(2, 3), new GamePosition[]{
                new GamePosition(1, 2),
        });
        tester.addVariant(new GamePosition(4, 5), new GamePosition[]{
                new GamePosition(1, 2),
                new GamePosition(3, 4),
        });
        return tester.test();
    }

    @TestFactory
    public Collection<DynamicTest> testScenarioOfQueenCaptureWhite1() {
        StringState state = new StringState(
                """
                -- Bp -- -- -- -- -- --
                Bp -- Wp -- Bp -- Bq --
                -- -- -- Bp -- -- -- --
                Wp -- Bp -- Wq -- Bp --
                -- -- -- -- -- -- -- --
                -- -- -- -- Bp -- -- --
                -- Bp -- Bp -- -- -- Wp
                -- -- -- -- -- -- -- --
                """
        );
        ScenarioTester tester = new ScenarioTester(state, GamePlayerType.White);
        tester.addStart(new GamePosition(3, 4));
        tester.addVariant(new GamePosition(0, 7), new GamePosition[]{
                new GamePosition(1, 6),
        });
        tester.addVariant(new GamePosition(2, 5), new GamePosition[0]);
        tester.addVariant(new GamePosition(4, 3), new GamePosition[0]);
        tester.addVariant(new GamePosition(5, 2), new GamePosition[0]);
        tester.addVariant(new GamePosition(7, 0), new GamePosition[]{
                new GamePosition(6, 1),
        });
        tester.addVariant(new GamePosition(4, 5), new GamePosition[0]);
        tester.addVariant(new GamePosition(5, 6), new GamePosition[0]);
        return tester.test();
    }

    @TestFactory
    public Collection<DynamicTest> testScenarioOfQueenCaptureWhite2() {
        StringState state = new StringState(
                """
                -- Bp -- -- -- -- -- --
                Bp -- Wp -- Bp -- Bp --
                -- -- -- Bp -- -- -- --
                Wp -- Bp -- Bq -- Bp --
                -- -- -- -- -- -- -- --
                -- -- -- -- Bp -- -- --
                -- Wq -- Bp -- -- -- Wp
                -- -- -- -- -- -- -- --
                """
        );
        ScenarioTester tester = new ScenarioTester(state, GamePlayerType.White);
        tester.addStart(new GamePosition(6, 1));
        tester.addVariant(new GamePosition(2, 5), new GamePosition[]{
                new GamePosition(3, 4),
        });
        tester.addVariant(new GamePosition(7, 0), new GamePosition[0]);
        tester.addVariant(new GamePosition(5, 0), new GamePosition[0]);
        tester.addVariant(new GamePosition(7, 2), new GamePosition[0]);
        tester.addVariant(new GamePosition(5, 2), new GamePosition[0]);
        tester.addVariant(new GamePosition(4, 3), new GamePosition[0]);
        return tester.test();
    }

    @TestFactory
    public Collection<DynamicTest> testScenarioOfQueenCaptureBlack1() {
        StringState state = new StringState(
                """
                -- -- -- -- -- -- -- --
                Bp -- -- -- Bp -- -- --
                -- -- -- Wp -- -- -- --
                -- -- -- -- -- -- Bp --
                -- -- -- -- -- Wp -- --
                -- -- -- -- Bp -- Bq --
                -- Wq -- Bp -- -- -- Wp
                -- -- -- -- -- -- -- --
                """
        );
        ScenarioTester tester = new ScenarioTester(state, GamePlayerType.Black);
        tester.addStart(new GamePosition(5, 6));
        tester.addVariant(new GamePosition(3, 4), new GamePosition[]{
                new GamePosition(4, 5),
        });
        tester.addVariant(new GamePosition(1, 2), new GamePosition[]{
                new GamePosition(4, 5),
                new GamePosition(2, 3),
        });
        tester.addVariant(new GamePosition(4, 7), new GamePosition[0]);
        tester.addVariant(new GamePosition(6, 5), new GamePosition[0]);
        tester.addVariant(new GamePosition(7, 4), new GamePosition[0]);
        return tester.test();
    }

    @TestFactory
    public Collection<DynamicTest> testScenarioOfQueenCaptureBlack2() {
        StringState state = new StringState(
                """
                -- -- -- -- -- -- -- Wp
                -- -- -- -- -- -- Wp --
                -- Wp -- -- -- -- -- --
                -- -- Wq -- -- -- Wp --
                -- -- -- -- -- -- -- --
                -- -- -- -- -- -- Wp --
                -- -- -- -- -- Bq -- --
                -- -- -- -- -- -- -- --
                """
        );
        ScenarioTester tester = new ScenarioTester(state, GamePlayerType.Black);
        tester.addStart(new GamePosition(6, 5));
        tester.addVariant(new GamePosition(4, 7), new GamePosition[]{
                new GamePosition(5, 6),
        });
        tester.addVariant(new GamePosition(2, 5), new GamePosition[]{
                new GamePosition(5, 6),
                new GamePosition(3, 6),
        });
        tester.addVariant(new GamePosition(7, 6), new GamePosition[0]);
        tester.addVariant(new GamePosition(7, 4), new GamePosition[0]);
        tester.addVariant(new GamePosition(5, 4), new GamePosition[0]);
        tester.addVariant(new GamePosition(4, 3), new GamePosition[0]);
        return tester.test();
    }

}