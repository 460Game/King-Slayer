package game.message;

import game.model.Game.GameModel;
import game.model.Game.Tile.Tile;

public class SetTileMessage implements ToClientMessage {

    private int x,y;
    private Tile tile;

    SetTileMessage() {

    }
    SetTileMessage(int x, int y, Tile tile) {
        this.tile = tile;
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute(GameModel model) {
        model.setTile(x,y,tile);
    }
}
