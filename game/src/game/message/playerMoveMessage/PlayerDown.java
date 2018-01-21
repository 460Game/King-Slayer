package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.Model.ServerGameModel;

public class PlayerDown extends ActionMessage {

    long id;

    public PlayerDown(long i) {
        super();
        id = i;
    }

    PlayerDown() {

    }

    @Override
    public void executeServer(ServerGameModel model) {
        model.getPlayer(id).down();
        model.processMessage(new SetEntityMessage(model.getPlayer(id)));
    }
}
