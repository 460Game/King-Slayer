package game.message;

import game.model.Game.Model.ClientGameModel;
import game.model.Game.Map.Tile;

public class SetTileMessage implements ToClientMessage {

    private int x,y;
    private Tile tile;

    public SetTileMessage() {

    }
    public SetTileMessage(int x, int y, Tile tile) {
        this.tile = tile;
        this.x = x;
        this.y = y;
    }

    @Override
    public void executeClient(ClientGameModel model) {
        model.setTile(x,y,tile);
    }
}
