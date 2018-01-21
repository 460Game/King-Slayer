package game.message;

import game.model.Game.Model.ClientGameModel;

public class StartGameMessage implements ToClientMessage {
    @Override
    public void executeClient(ClientGameModel model) {
        model.start();
    }
}
