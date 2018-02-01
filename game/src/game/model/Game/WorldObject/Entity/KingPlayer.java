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

    // Get the images to represent the king on each team.
    private static Map<String, Point> imageMap = new HashMap<>();
    static {
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
                gc.drawImage(imageRedKing,
                    p.x * 32, p.y * 32, 32, 32,
                    this.getX() * TILE_PIXELS, this.getY() * TILE_PIXELS - 2 * 32 + 2 * TILE_PIXELS, 64, 64);
            } else {
                gc.drawImage(imageBlueKing,
                    p.x * 32, p.y * 32, 32, 32,
                    this.getX() * TILE_PIXELS, this.getY() * TILE_PIXELS - 2 * 32 + 2 * TILE_PIXELS, 64, 64);
            }
        } catch (Exception e) {

        }
    }

}
