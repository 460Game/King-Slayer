package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.GameModel;

public class PlayerUp extends ActionMessage {


    boolean p;
    public PlayerUp() {
        super();
        p = false;
    }
    public PlayerUp(int i) {
        super();
        p = i == 0;
    }



    @Override
    public void execute(GameModel model) {
        if(p) {
            model.playerA.up();
            model.processMessage(new SetEntityMessage(model.playerA));
        } else {
            model.playerB.up();
            model.processMessage(new SetEntityMessage(model.playerB));
        }
    }
}
