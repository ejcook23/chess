package webSocketMessages.serverMessages;

import chess.ChessBoard;
import model.GameData;

public class LoadGame extends ServerMessage{

    GameData game;

    public GameData getGame() {
        return game;
    }

    public LoadGame(ServerMessageType type, GameData game) {
        super(type);
        this.game = game;

    }
}
