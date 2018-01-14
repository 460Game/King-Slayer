package game.message;

import game.model.Game.GameModel;

public interface Message {
    default boolean sendToServer(){
        return false;
    }
    default boolean sendToClient(){
        return false;
    }

    void execute(GameModel model);
}
