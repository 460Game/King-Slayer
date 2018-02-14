package game.message.toClient;

import game.message.Message;
import game.model.game.model.ClientGameModel;
import game.model.game.model.GameModel;

/**
 * Interface for a message to be sent from the server to a client.
 */
public interface ToClientCommand extends Message {

    /**
     * Return true because this is a message sent from the server
     * to a client.
     * @return true
     */
    default boolean sendToClient() {
        return true;
    }

    /**
     * Executes the message on the given game model.
     * @param model game model to execute the message
     */
    default void execute(GameModel model) {
        this.executeClient((ClientGameModel) model);
    }

    /**
     * Executes the message on the client.
     * @param model the game model on the client
     */
    void executeClient(ClientGameModel model);
}
