package game.message;

public interface ToClientMessage extends Message {

    default boolean sendToClient() {
        return true;
    }
}
