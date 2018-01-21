package game.message;

import game.model.Game.Model.GameModel;
import game.model.Game.Model.Model;

public interface Message {
    default boolean sendToServer(){
        return false;
    }
    default boolean sendToClient(){
        return false;
    }

    default void execute(Model model) {
        model.processMessage(this);
    }

    void execute(GameModel model);
}
