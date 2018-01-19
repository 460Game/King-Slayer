package game.model;

import game.model.Game.GameModel;
import game.model.Game.ServerMapGenerator;

import java.util.Collection;

public class ServerGameModel extends GameModel {

    public ServerGameModel(Collection<? extends IModel> clients) {
        super(true, clients, new ServerMapGenerator(Util.Const.GRID_X_SIZE, Util.Const.GRID_Y_SIZE));

    }

}
