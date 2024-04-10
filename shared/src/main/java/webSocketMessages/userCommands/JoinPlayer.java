package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{

    Integer gameID;
    ChessGame.TeamColor playerColor;
    CommandType commandType;

    public ChessGame.TeamColor getTeamColor() {
        return playerColor;
    }

    public JoinPlayer(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.commandType = CommandType.JOIN_PLAYER;
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
