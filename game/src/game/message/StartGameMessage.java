package game.message;

import game.model.Game.Model.ClientGameModel;

/**
 * Message sent to start the client's game. This message is sent
 * by the server.
 */
public class StartGameMessage implements ToClientMessage {

    /**
     * Starts the client's game model.
     * @param model the game model on the client
     */
    @Override
    public void executeClient(ClientGameModel model) {
        model.start();
    }
}
