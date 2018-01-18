package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.GameModel;

public class PlayerRight extends ActionMessage {


    boolean p;
    public PlayerRight() {
        super();
    }
    public PlayerRight(int i) {
        super();
        p = i == 0;
    }



    @Override
    public void execute(GameModel model) {
        if(p) {
            model.playerA.right();
            model.processMessage(new SetEntityMessage(model.playerA));
        } else {
            model.playerB.right();
            model.processMessage(new SetEntityMessage(model.playerB));
        }
    }
}
