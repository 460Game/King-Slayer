package game.model;

import game.model.Game.GameModel;
import game.model.Game.MapGenerator;

import java.util.Collections;

public class ClientGameModel extends GameModel {
    public ClientGameModel(IModel server, MapGenerator generator) {
        super(false, Collections.singleton(server), generator);
    }
}
