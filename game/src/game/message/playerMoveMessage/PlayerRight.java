package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.Model.ServerGameModel;

public class PlayerRight extends ActionMessage {


    long id;
    public PlayerRight(long i) {
        super();
        id = i;
    }

    PlayerRight(){

    }


    @Override
    public void executeServer(ServerGameModel model) {

        model.getPlayer(id).right();
            model.processMessage(new SetEntityMessage(model.getPlayer(id)));
    }
}
