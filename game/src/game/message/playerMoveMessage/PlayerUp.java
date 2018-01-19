package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.GameModel;
import game.model.Game.WorldObject.Entity.TestPlayer;

public class PlayerUp extends ActionMessage {


    long id;
    public PlayerUp(long i) {
        super();
        id = i;
    }

    PlayerUp(){

    }


    @Override
    public void execute(GameModel model) {
        if(model.getPlayers().get(0).getId() == id) {
            model.getPlayers().get(0).up();
            model.processMessage(new SetEntityMessage(model.getPlayers().get(0)));
        } else {
            model.getPlayers().get(1).up();
            model.processMessage(new SetEntityMessage(model.getPlayers().get(1)));
        }
    }
}
