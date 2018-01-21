package game.message;

import game.model.Game.Model.GameModel;
import game.model.Game.Model.Model;

/**
 * Interface for a generic message to be sent between clients and server.
 */
public interface Message {

    /**
     * Return true if the message is intended to be sent to the server.
     * Return false otherwise.
     * @return true if the message is sent to the server
     */
    default boolean sendToServer(){
        return false;
    }

    /**
     * Return true if the message is intended to be sent to clients.
     * Return false otherwise.
     * @return true if the message is sent to clients
     */
    default boolean sendToClient(){
        return false;
    }

    /**
     * Executes the message in the given game model.
     * @param model game model to execute the message
     */
    void execute(GameModel model);
}
