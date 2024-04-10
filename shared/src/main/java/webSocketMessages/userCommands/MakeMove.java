package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{

    Integer gameID;
    CommandType command = CommandType.MAKE_MOVE;
    ChessMove move;

    public CommandType getCommand() {
        return command;
    }

    public Integer getGameID() {
        return gameID;
    }

    public MakeMove(String authToken, Integer gameID, ChessMove move) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;

    }
}

