package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.Model.ServerGameModel;

public class PlayerLeft extends ActionMessage {

    long id;

    public PlayerLeft(long i) {
        super();
        id = i;
    }

    PlayerLeft(){

    }

    @Override
    public void executeServer(ServerGameModel model) {
        model.getPlayer(id).left();
           model.processMessage(new SetEntityMessage(model.getPlayer(id)));
    }
}
