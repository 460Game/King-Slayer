package game.message;

import game.message.Message;
import game.model.ClientGameModel;
import game.model.Game.GameModel;
import game.model.Game.WorldObject.Entity.TestPlayer;
import game.view.ClientView;

public class CreatePlayerMessage implements ToClientMessage {

    private TestPlayer playerA;
    public CreatePlayerMessage(TestPlayer playerA) {
        this.playerA = playerA;
    }

    @Override
    public void execute(GameModel model) {
        ((ClientGameModel)model).createPlayer(playerA);
    }

    CreatePlayerMessage(){

    }
}
