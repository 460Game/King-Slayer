package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.Model.ServerGameModel;

public class PlayerUp extends ActionMessage {


    long id;

    public PlayerUp(long i) {
        super();
        id = i;
    }

    PlayerUp() {

    }


    @Override
    public void executeServer(ServerGameModel model) {
        model.getPlayer(id).up();
        model.processMessage(new SetEntityMessage(model.getPlayer(id)));
    }
}
