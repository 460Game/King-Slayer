package game.model;

import game.message.SetTileMessage;
import game.model.Game.GameModel;
import game.model.Game.MapGenerator;
import game.model.Game.ServerMapGenerator;
import game.model.Game.WorldObject.TestPlayer;

import java.util.Collection;

public class ServerGameModel extends GameModel {

    public ServerGameModel(Collection<? extends IModel> clients) {
        super(true, clients, new ServerMapGenerator(GameModel.GRID_X_SIZE, GameModel.GRID_Y_SIZE));

        for(IModel client : clients) {
            for(int i = 0; i < this.getMapWidth(); i++)
                for(int j = 0; j < this.getMapWidth(); j++)
                    client.processMessage(new SetTileMessage(i,j, this.getTile(i,j)));
        }
    }


}
