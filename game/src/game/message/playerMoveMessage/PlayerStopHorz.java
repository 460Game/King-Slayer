package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.GameModel;

public class PlayerStopHorz extends ActionMessage {


    long id;
    public PlayerStopHorz(long i) {
        super();
        id = i;
    }

    PlayerStopHorz() {

    }


    @Override
    public void execute(GameModel model) {
        if(model.getPlayers().get(0).getId() == id) {
            model.getPlayers().get(0).stopHorz();
            model.processMessage(new SetEntityMessage(model.getPlayers().get(0)));
        } else {
            model.getPlayers().get(1).stopHorz();
            model.processMessage(new SetEntityMessage(model.getPlayers().get(1)));
        }
    }
}
