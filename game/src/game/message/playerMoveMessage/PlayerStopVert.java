package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.GameModel;

public class PlayerStopVert extends ActionMessage {

    boolean p;
    public PlayerStopVert() {
        super();
    }
    public PlayerStopVert(int i) {
        super();
        p = i == 0;
    }

    @Override
    public void execute(GameModel model) {
        if(p) {
            model.playerA.stopVert();
            model.processMessage(new SetEntityMessage(model.playerA));
        } else {
            model.playerB.stopVert();
            model.processMessage(new SetEntityMessage(model.playerB));
        }
    }
}
