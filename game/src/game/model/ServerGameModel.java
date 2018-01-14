package game.model;

import game.model.Game.GameModel;
import game.model.Game.MapGenerator;
import game.model.Game.WorldObject.TestPlayer;

import java.util.Collection;

public class ServerGameModel extends GameModel {

    public ServerGameModel(Collection<IModel> clients) {
        super(true, clients, new MapGenerator(GameModel.GRID_X_SIZE, GameModel.GRID_Y_SIZE));
    }


}
