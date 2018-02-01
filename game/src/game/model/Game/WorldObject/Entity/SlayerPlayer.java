package game.model.Game.WorldObject.Entity;

import game.model.Game.Map.Tile;
import game.model.Game.Model.GameModel;
import game.model.Game.WorldObject.Team;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * Defines the slayer of a team.
 */
public class SlayerPlayer extends Player {

    /**
     * Image of the slayer for the red team.
     */
    static Image imageRedSlayer;

    /**
     * Image of the slayer for the blue team.
     */
    static Image imageBlueSlayer;

    // Gets the images to represent the slayer on each team.
    static {
        try {
            imageRedSlayer = new Image(Tile.class.getResource("slayer_red_1.png").openStream());
            imageBlueSlayer = new Image(Tile.class.getResource("slayer_blue_1.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(GraphicsContext gc, GameModel model) {
        //gc.setFill(this.getTeam().color);
        //shape.draw(gc);
        if (this.getTeam() == Team.ONE)
            draw(gc, imageRedSlayer);
        else
            draw(gc, imageBlueSlayer);
    }

}
