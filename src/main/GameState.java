package main;

public class GameState {

    static {
        System.loadLibrary("Utp_Game_Project_Logic");
    }

    public record Initializer (GamePawnType[] state, GamePlayerType player) {}

    public GameState() { init(); }

    public GameState(GamePawnType[] state, GamePlayerType currentPlayer) { init(state, currentPlayer); }

    public GameState(Initializer initializer) { init(initializer.state, initializer.player); }

    public native void init();

    public native void init(GamePawnType[] state, GamePlayerType currentPlayer);

    public native GameMoveResult process(GamePosition from, GamePosition to);

    public native GamePawnType get(GamePosition position);

    public native void reset();

    public native GamePlayerType getCurrentPlayer();

    public native int getWhitePawnsAmount();

    public native int getBlackPawnsAmount();

}
