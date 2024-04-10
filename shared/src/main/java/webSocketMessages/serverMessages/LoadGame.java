package webSocketMessages.serverMessages;

import chess.ChessBoard;
import model.GameData;

public class LoadGame extends ServerMessage{

    ChessBoard game;

    public ChessBoard getGame() {
        return game;
    }

    public LoadGame(ServerMessageType type, ChessBoard game) {
        super(type);
        this.game = game;

    }
}
