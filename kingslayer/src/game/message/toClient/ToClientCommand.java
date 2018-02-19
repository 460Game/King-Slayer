package game.message.toClient;

import game.message.Message;
import game.model.game.model.ClientGameModel;
import game.model.game.model.GameModel;

/**
 * Interface for a command to be sent from the server to a client.
 */
public interface ToClientCommand extends Message {

    /**
     * Return true because this is a command sent from the server
     * to a client.
     * @return true
     */
    default boolean sendToClient() {
        return true;
    }

    /**
     * Executes the command on the given game model. Game model
     * is given to be a client game model.
     * @param model game model to execute the command
     */
    default void execute(GameModel model) {
        this.executeClient((ClientGameModel) model);
    }

    /**
     * Executes the command on the client.
     * @param model the game model on the client
     */
    void executeClient(ClientGameModel model);
}
