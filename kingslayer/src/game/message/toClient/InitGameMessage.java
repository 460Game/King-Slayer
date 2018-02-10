package game.message.toClient;

import game.model.game.model.ClientGameModel;

/*
sent after server has sent all intial data
 */
public class InitGameMessage implements ToClientMessage {
    @Override
    public void executeClient(ClientGameModel model) {
        model.getAllCells().forEach(cell->cell.initDraw(model));
    }
}
