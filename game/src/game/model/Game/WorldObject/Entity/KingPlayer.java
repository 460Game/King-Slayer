package game.model.Game.WorldObject.Entity;

import game.model.Game.Map.Tile;
import game.model.Game.WorldObject.Team;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;

public class KingPlayer extends Player {
  static Image imageRedKing;
  static Image imageBlueKing;

  static {
    try {
      imageRedKing = new Image(Tile.class.getResource("king_red_1.png").openStream());
      imageBlueKing = new Image(Tile.class.getResource("king_blue_1.png").openStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void draw(GraphicsContext gc) {
    //gc.setFill(this.getTeam().color);
    //shape.draw(gc);
    if (this.getTeam() == Team.ONE) {
      draw(gc, imageRedKing);
    } else {
      draw(gc, imageBlueKing);
    }
  }

}
