package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.GameModel;

public class PlayerDown extends ActionMessage {

    boolean p;
    public PlayerDown() {
        super();
    }
    public PlayerDown(int i) {
        super();
        p = i == 0;
    }


    @Override
    public void execute(GameModel model) {
        if(p) {
            model.playerA.down();
            model.processMessage(new SetEntityMessage(model.playerA));
        } else {
            model.playerB.down();
            model.processMessage(new SetEntityMessage(model.playerB));
        }
    }
}
