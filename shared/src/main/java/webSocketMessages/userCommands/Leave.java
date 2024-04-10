package webSocketMessages.userCommands;

public class Leave extends UserGameCommand{

    Integer gameID;
    CommandType command = CommandType.LEAVE;

    public CommandType getCommand() {
        return command;
    }

    public Integer getGameID() {
        return gameID;
    }

    public Leave(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
    }
}

