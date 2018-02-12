package game.message.toClient;

import game.model.game.model.ClientGameModel;

/**
 * This message is sent to the client after the server has sent
 * all the initial data.
 */
public class InitGameMessage implements ToClientMessage {

    @Override
    public void executeClient(ClientGameModel model) {
        model.getAllCells().forEach(cell -> cell.initDraw(model));
    }
}
