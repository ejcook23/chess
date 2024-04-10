package webSocketMessages.userCommands;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.JOIN_OBSERVER;

public class JoinObserver extends UserGameCommand{

    Integer gameID;

    public Integer getGameID() {
        return gameID;
    }

    public JoinObserver(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
        commandType = JOIN_OBSERVER;
    }
}

