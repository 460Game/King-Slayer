package game.message;

import game.model.Game.Model.GameModel;
import game.model.Game.Model.ServerGameModel;

public interface ToServerMessage extends Message {
    default boolean sendToServer() {
        return true;
    }

    default void execute(GameModel model) {
        this.executeServer((ServerGameModel)model);
    }

    abstract void executeServer(ServerGameModel model);
}
