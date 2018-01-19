package game.model;

import game.model.Game.GameModel;
import game.model.Game.MapGenerator;
import game.model.Game.WorldObject.Entity.TestPlayer;

import java.util.Collections;

public class ClientGameModel extends GameModel {
    public ClientGameModel(IModel server, MapGenerator generator) {
        super(false, Collections.singletonList(server), generator);
    }

    private TestPlayer localPlayer = null;

    public TestPlayer getLocalPlayer() {
        return localPlayer;
    }

    public void createPlayer(TestPlayer playerA) {
        this.setEntity(playerA);
        localPlayer = playerA;
    }
}
