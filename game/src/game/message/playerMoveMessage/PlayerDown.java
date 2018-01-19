package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.GameModel;
import game.model.Game.WorldObject.Entity.TestPlayer;

public class PlayerDown extends ActionMessage {

    long id;
    public PlayerDown(long i) {
        super();
        id = i;
    }

    PlayerDown(){

    }

    @Override
    public void execute(GameModel model) {
        if(model.getPlayers().get(0).getId() == id) {
            model.getPlayers().get(0).down();
            model.processMessage(new SetEntityMessage(model.getPlayers().get(0)));
        } else {
            model.getPlayers().get(1).down();
            model.processMessage(new SetEntityMessage(model.getPlayers().get(1)));
        }
    }
}
