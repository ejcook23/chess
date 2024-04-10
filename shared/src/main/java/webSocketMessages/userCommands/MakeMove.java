package webSocketMessages.userCommands;

import chess.ChessMove;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.MAKE_MOVE;

public class MakeMove extends UserGameCommand{

    Integer gameID;
    ChessMove move;

    public Integer getGameID() {
        return gameID;
    }

    public MakeMove(String authToken, Integer gameID, ChessMove move) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
        commandType = MAKE_MOVE;

    }
}

