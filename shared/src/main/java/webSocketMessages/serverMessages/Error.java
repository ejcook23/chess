package webSocketMessages.serverMessages;

import static webSocketMessages.serverMessages.ServerMessage.ServerMessageType.ERROR;

public class Error extends ServerMessage{

    String errorMessage;

    public ServerMessageType getMessage() {
        return message;
    }

    ServerMessageType message = ERROR;

    public String getErrorMessage() {
        return errorMessage;
    }

    public Error(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;

    }
}
