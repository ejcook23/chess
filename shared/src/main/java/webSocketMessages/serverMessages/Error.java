package webSocketMessages.serverMessages;


public class Error extends ServerMessage{

    String errorMessage;

    public String getMessage() {
        return errorMessage;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public Error(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;

    }
}
