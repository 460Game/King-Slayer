package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.GameModel;

public class PlayerDown extends ActionMessage {
    @Override
    public void execute(GameModel model) {
        model.playerA.down();
        model.processMessage(new SetEntityMessage(model.playerA));
    }
}
