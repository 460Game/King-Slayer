package game.message.toServer;

import game.message.Message;
import game.model.game.model.GameModel;
import game.model.game.model.ServerGameModel;

/**
 * Interface for a request to be sent from a client to the server.
 */
public interface ToServerRequest extends Message {

    /**
     * Return true because this is a request sent from a client to the
     * server.
     * @return true
     */
    default boolean sendToServer() {
        return true;
    }

    /**
     * Executes the request on the given game model.
     * @param model game model to execute the request
     */
    default void execute(GameModel model) { this.executeServer((ServerGameModel) model); }

    /**
     * Executes the request on the game server.
     * @param model the game model on the game server
     */
    void executeServer(ServerGameModel model);
}
