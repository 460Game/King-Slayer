package game.message;

import game.model.Game.Model.GameModel;
import game.model.Game.Model.ServerGameModel;

/**
 * Interface for a message to be sent from a client to the server.
 */
public interface ToServerMessage extends Message {

    /**
     * Return true because this is a message sent from a client to the
     * server.
     * @return true
     */
    default boolean sendToServer() {
        return true;
    }

    /**
     * Executes the message on the given game model.
     * @param model game model to execute the message
     */
    default void execute(GameModel model) { this.executeServer((ServerGameModel) model); }

    /**
     * Executes the message on the game server.
     * @param model the game model on the game server
     */
    void executeServer(ServerGameModel model);
}
