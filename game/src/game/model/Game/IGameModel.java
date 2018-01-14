package game.model.Game;

import game.model.Game.Tile.Tile;
import game.model.IModel;
import javafx.scene.canvas.GraphicsContext;

public interface IGameModel extends IModel {


    public abstract void setTile(int x, int y, Tile tile);
    public abstract void update();
    public abstract void draw(GraphicsContext gc);

}
