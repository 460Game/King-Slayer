package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.GameModel;

public class PlayerStopHorz extends ActionMessage {


    boolean p;
    public PlayerStopHorz(int i) {
        super();
        p = i == 0;
    }



    @Override
    public void execute(GameModel model) {
        if(p) {
            model.playerA.stopHorz();
            model.processMessage(new SetEntityMessage(model.playerA));
        } else {
            model.playerB.stopHorz();
            model.processMessage(new SetEntityMessage(model.playerB));
        }
    }
}
