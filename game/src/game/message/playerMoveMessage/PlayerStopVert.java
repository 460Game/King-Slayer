package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.Model.ServerGameModel;

public class PlayerStopVert extends ActionMessage {

    long id;

    public PlayerStopVert(long i) {
        super();
        id = i;
    }

    PlayerStopVert() {

    }

    @Override
    public void executeServer(ServerGameModel model) {
        model.getPlayer(id).stopVert();
        model.processMessage(new SetEntityMessage(model.getPlayer(id)));
    }
}
