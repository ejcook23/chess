package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{

    Integer gameID;
    CommandType command = CommandType.JOIN_OBSERVER;

    public CommandType getCommand() {
        return command;
    }

    public Integer getGameID() {
        return gameID;
    }

    public JoinObserver(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
    }
}

