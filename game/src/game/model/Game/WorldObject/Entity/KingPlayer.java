package game.model.Game.WorldObject.Entity;

import game.model.Game.Map.Tile;
import game.model.Game.Model.GameModel;
import game.model.Game.WorldObject.Team;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static Util.Const.TILE_PIXELS;

/**
 * Defines the king of a team.
 */
public class KingPlayer extends Player {

    /**
     * Image that represents the king on the red team.
     */
    private static Image imageRedKing;

    /**
     * Image that represents the king on the blue team.
     */
    private static Image imageBlueKing;

    /**
     * Key for figuring out which piece of the character sheet is needed for
     * the king at a given point in time.
     */
    private static Map<String, Point> imageMap = new HashMap<>();

    static {
        // Read in file detailing which images to use at which points in time in the animation
        // of the king.
        Scanner input;
        try {
            input = new Scanner(Tile.class.getResource("players.txt").openStream());

            while (input.hasNext()) {
                Point curPoint = new Point(input.nextInt(), input.nextInt());
                if (curPoint.x == -1) break;
                input.nextLine();
                String info = input.nextLine();
                imageMap.put(info, curPoint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Get the sprite sheets for the kings.
        try {
            imageRedKing = new Image(Tile.class.getResource("king_red_sheet_new.png").openStream());
            imageBlueKing = new Image(Tile.class.getResource("king_blue_sheet_new.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(GraphicsContext gc, GameModel model) {
        try {
            Point p = imageMap.get(imageNum + "" + direction);
            if (this.getTeam() == Team.ONE) {
                super.draw(gc, imageRedKing, p);
            } else {
                super.draw(gc, imageBlueKing, p);
            }
        } catch (Exception e) {

        }
    }

}
