package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.GameModel;

public class PlayerLeft extends ActionMessage {

    boolean p;
    public PlayerLeft() {
        super();
    }
    public PlayerLeft(int i) {
        super();
        p = i == 0;
    }


    @Override
    public void execute(GameModel model) {
        if(p) {
            model.playerA.left();
            model.processMessage(new SetEntityMessage(model.playerA));
        } else {
            model.playerB.left();
            model.processMessage(new SetEntityMessage(model.playerB));
        }
    }
}
