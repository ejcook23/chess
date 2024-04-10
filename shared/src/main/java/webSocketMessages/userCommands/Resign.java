package webSocketMessages.userCommands;

public class Resign extends UserGameCommand{

    Integer gameID;
    CommandType command = CommandType.RESIGN;

    public CommandType getCommand() {
        return command;
    }

    public Integer getGameID() {
        return gameID;
    }

    public Resign(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
    }
}

