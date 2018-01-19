package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.GameModel;

public class PlayerStopVert extends ActionMessage {

    long id;
    public PlayerStopVert(long i) {
        super();
        id = i;
    }

    PlayerStopVert(){

    }

    @Override
    public void execute(GameModel model) {
        if(model.getPlayers().get(0).getId() == id) {
            model.getPlayers().get(0).stopVert();
            model.processMessage(new SetEntityMessage(model.getPlayers().get(0)));
        } else {
            model.getPlayers().get(1).stopVert();
            model.processMessage(new SetEntityMessage(model.getPlayers().get(1)));
        }
    }
}
