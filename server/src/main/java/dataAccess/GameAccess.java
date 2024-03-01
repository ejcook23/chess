package dataAccess;

import chess.ChessGame;

public interface GameAccess {

    void addGame(int gameID, String whiteUser, String blackUser, String gameName, ChessGame game);
}
