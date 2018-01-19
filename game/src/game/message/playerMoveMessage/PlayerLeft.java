package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.GameModel;
import game.model.Game.WorldObject.Entity.TestPlayer;

public class PlayerLeft extends ActionMessage {

    long id;
    public PlayerLeft(long i) {
        super();
        id = i;
    }

    PlayerLeft(){

    }

    @Override
    public void execute(GameModel model) {
        if(model.getPlayers().get(0).getId() == id) {
            model.getPlayers().get(0).left();
            model.processMessage(new SetEntityMessage(model.getPlayers().get(0)));
        } else {
            model.getPlayers().get(1).left();
            model.processMessage(new SetEntityMessage(model.getPlayers().get(1)));
        }
    }
}
