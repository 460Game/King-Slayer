package game.model;

import game.model.Game.GameModel;

import java.util.Collection;

public class ServerGameModel extends GameModel {
    public ServerGameModel(Collection<IModel> clients) {
        super(true, clients);
    }
}
