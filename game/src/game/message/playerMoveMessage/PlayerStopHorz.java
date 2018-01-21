package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.Model.ServerGameModel;

public class PlayerStopHorz extends ActionMessage {


    long id;

    public PlayerStopHorz(long i) {
        super();
        id = i;
    }

    PlayerStopHorz() {

    }


    @Override
    public void executeServer(ServerGameModel model) {
        model.getPlayer(id).stopHorz();
        model.processMessage(new SetEntityMessage(model.getPlayer(id)));
    }
}
