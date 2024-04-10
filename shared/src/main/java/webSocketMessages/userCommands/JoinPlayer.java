package webSocketMessages.userCommands;

import chess.ChessGame;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.JOIN_PLAYER;

public class JoinPlayer extends UserGameCommand{

    Integer gameID;
    ChessGame.TeamColor playerColor;
    CommandType command = JOIN_PLAYER;

    @Override
    public CommandType getCommandType() {
        return command;
    }

    public ChessGame.TeamColor getTeamColor() {
        return playerColor;
    }

    public JoinPlayer(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }


}
