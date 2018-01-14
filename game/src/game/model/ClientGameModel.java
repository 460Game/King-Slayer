package game.model;

import game.model.Game.GameModel;

import java.util.Collections;

public class ClientGameModel extends GameModel {
    public ClientGameModel(IModel server) {
        super(false, Collections.singleton(server));
    }
}
