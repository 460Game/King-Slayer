package game.message;

import com.esotericsoftware.minlog.Log;
import game.model.Game.Model.ClientGameModel;
import game.model.Game.WorldObject.Entity.TestPlayer;

public class SetPlayerMessage implements ToClientMessage {

    private long playerId;
    public SetPlayerMessage(long playerId) {
        this.playerId = playerId;
    }


    public SetPlayerMessage(TestPlayer player) {
        this.playerId = player.getId();
    }

    @Override
    public void executeClient(ClientGameModel model) {
        model.setLocalPlayer(playerId);
    }

    SetPlayerMessage(){

    }
}
