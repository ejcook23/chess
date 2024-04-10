package webSocketMessages.userCommands;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.JOIN_PLAYER;
import static webSocketMessages.userCommands.UserGameCommand.CommandType.RESIGN;

public class Resign extends UserGameCommand{

    Integer gameID;


    public Integer getGameID() {
        return gameID;
    }

    public Resign(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
        commandType = RESIGN;
    }
}

