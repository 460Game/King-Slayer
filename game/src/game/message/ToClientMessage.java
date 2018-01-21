package game.message;

import game.model.Game.Model.ClientGameModel;
import game.model.Game.Model.GameModel;

public interface ToClientMessage extends Message {

    default boolean sendToClient() {
        return true;
    }

    default void execute(GameModel model) {
        this.executeClient((ClientGameModel)model);
    }

    abstract void executeClient(ClientGameModel model);
}
