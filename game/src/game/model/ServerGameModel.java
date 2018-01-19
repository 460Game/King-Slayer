package game.model;

import game.model.Game.GameModel;
import game.model.Game.ServerMapGenerator;

import java.util.Collection;
import static Util.Const.*;

public class ServerGameModel extends GameModel {

    public ServerGameModel(Collection<? extends IModel> clients) {
        super(true, clients, new ServerMapGenerator(GRID_X_SIZE, GRID_Y_SIZE));

    }

}
