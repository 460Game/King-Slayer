package game.message;

import game.model.Game.GameModel;
import game.model.IModel;

public interface Message {
    default boolean sendToServer(){
        return false;
    }
    default boolean sendToClient(){
        return false;
    }

    default void execute(IModel model) {
        model.processMessage(this);
    }

    void execute(GameModel model);
}
