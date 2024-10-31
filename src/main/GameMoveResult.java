package main;

/**
 * Contains results of the native move process.
 * @param isCorrect Tells whether move was a proper validated move and game state updated
 * @param isQueen Tells whether the move transformed a pawn into the queen
 * @param takenPawns Tells the positions of the pawns, which were captured by the move
 * @param winner Tells who have won the game. If set to null game is ongoing
 * @param message Holds the message of an error if such arisen
 */
public record GameMoveResult (
        boolean isCorrect,
        boolean isQueen,
        GamePosition[] takenPawns,
        GamePlayerType winner,
        String message
) {}