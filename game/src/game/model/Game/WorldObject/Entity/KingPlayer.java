package game.model.Game.WorldObject.Entity;

import game.model.Game.Map.Tile;
import game.model.Game.Model.GameModel;
import game.model.Game.WorldObject.Team;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * Defines the king of a team.
 */
public class KingPlayer extends Player {
    private static Image imageRedKing;
    private static Image[][] imagesBlueKing = new Image[4][4];

    private int imageNum = 0;
    private int direction = 0;
    private int count = 0;

    static {
        try {
            imageRedKing = new Image(Tile.class.getResource("king_red_1.png").openStream());

      /* Blue King */
            // Front
            imagesBlueKing[0][0] = new Image(Tile.class.getResource("king_blue_0.png").openStream());
            imagesBlueKing[0][1] = new Image(Tile.class.getResource("king_blue_1.png").openStream());
            imagesBlueKing[0][2] = imagesBlueKing[0][0];
            imagesBlueKing[0][3] = new Image(Tile.class.getResource("king_blue_2.png").openStream());

            // Right
            imagesBlueKing[1][0] = new Image(Tile.class.getResource("king_blue_right_0.png").openStream());
            imagesBlueKing[1][1] = new Image(Tile.class.getResource("king_blue_right_1.png").openStream());
            imagesBlueKing[1][2] = imagesBlueKing[1][0];
            imagesBlueKing[1][3] = new Image(Tile.class.getResource("king_blue_right_2.png").openStream());

            // Left
            imagesBlueKing[2][0] = new Image(Tile.class.getResource("king_blue_left_0.png").openStream());
            imagesBlueKing[2][1] = new Image(Tile.class.getResource("king_blue_left_1.png").openStream());
            imagesBlueKing[2][2] = imagesBlueKing[2][0];
            imagesBlueKing[2][3] = new Image(Tile.class.getResource("king_blue_left_2.png").openStream());

            // Back
            imagesBlueKing[3][0] = new Image(Tile.class.getResource("king_blue_back_0.png").openStream());
            imagesBlueKing[3][1] = new Image(Tile.class.getResource("king_blue_back_1.png").openStream());
            imagesBlueKing[3][2] = imagesBlueKing[3][0];
            imagesBlueKing[3][3] = new Image(Tile.class.getResource("king_blue_back_2.png").openStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(GraphicsContext gc, GameModel model) {
        //gc.setFill(this.getTeam().color);
        //shape.draw(gc);
        if (this.getTeam() == Team.ONE) {
            draw(gc, imageRedKing);
        } else {
            draw(gc, imagesBlueKing[direction][imageNum]);
        }
    }

    @Override
    public void update(long time, GameModel model) {
        super.update(time, model);

        // Update direction of image
        double angle = getMovementAngle();
        if (angle >= -0.75 * Math.PI && angle < -0.25 * Math.PI) {
            direction = 3;
        } else if (angle >= -0.25 * Math.PI && angle < 0.25 * Math.PI) {
            direction = 1;
        } else if (angle >= 0.25 * Math.PI && angle < 0.75 * Math.PI) {
            direction = 0;
        } else if (angle >= 0.75 * Math.PI || angle < -0.75 * Math.PI) {
            direction = 2;
        }

        // Update image being used
        if (this.getSpeed() != 0) {
            count++;
            if (count > 11) {
                count = 0;
                imageNum = (imageNum + 1) % imagesBlueKing[direction].length;
            }
        } else {
            imageNum = 0;
        }
    }

}
