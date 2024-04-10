package webSocketMessages.userCommands;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.LEAVE;

public class Leave extends UserGameCommand{

    Integer gameID;

    public Integer getGameID() {
        return gameID;
    }

    public Leave(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
        commandType = LEAVE;
    }
}

