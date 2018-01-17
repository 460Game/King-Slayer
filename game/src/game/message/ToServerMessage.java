package game.message;

public interface ToServerMessage extends Message {
    default boolean sendToServer() {
        return true;
    }
}
