package game.model.game.grid;

import game.model.game.model.GameModel;
import game.model.game.map.Tile;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.collideStrat.CollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.Hitbox;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.SIGHT_RADIUS;
import static game.model.game.model.worldObject.entity.Entity.EntityProperty.TEAM;
import static images.Images.TILE_IMAGE;
import static util.Util.toDrawCoords;

/**
 * Defines an individual cell on the game grid. Knows the entities
 * that currently exist on the cell and the type of tile that it
 * currently is.
 */
public class GridCell {

    /**
     * List of entities that currently reside on the cell.
     */
    private ArrayList<Entity> contents = new ArrayList<>();

    /**
     * X-coordinate of the top left of this cell.
     */
    private int x;

    /**
     * Y-coordinate of the top left of this cell.
     */
    private int y;

    /**
     * Type of tile that the grid is currently.
     */
    private Tile tile;

    public int[] losRanges;
    private boolean[] explored;

    /**
     * Constructor for a grid cell.
     *
     * @param x x-coordinate of the top left of the cell
     * @param y y-coordinate of the top left of the cell
     */
    public GridCell(int x, int y) {
        this.x = x;
        this.y = y;
        this.tile = Tile.UNSET;
        losRanges = new int[Team.values().length];
        explored = new boolean[Team.values().length];
        for (Team team : Team.values()) {
            losRanges[team.team] = 0;
            explored[team.team] = false;
        }
    }

    /**
     * Default constructor needed for serialization.
     */
    public GridCell() {

    }


    public Stream<GridCell> getNeighbors(GameModel model) {
        Stream.Builder<GridCell> builder = Stream.builder();
        if(this.x > 0)
        builder.add(model.getCell(this.x - 1, y));
        if(this.x < model.getMapWidth() - 1)
        builder.add(model.getCell(this.x + 1, y));
        if(this.y > 0)
        builder.add(model.getCell(this.x , y - 1));
        if(this.y < model.getMapHeight() - 1)
        builder.add(model.getCell(this.x, this.y + 1));
        return builder.build();
    }

    public void updatePeriodicLOS(GameModel gameModel) {

        boolean passable = this.isSeeThrough();

        for (Team team : Team.values()) {
            losRanges[team.team] = 0;
        }

        this.streamContents().filter(entity -> entity.has(TEAM) && entity.has(SIGHT_RADIUS)).forEach(entity -> {
            int sr = entity.get(SIGHT_RADIUS);
            int tm = entity.getTeam().team;
            if (losRanges[tm] < sr) {
                losRanges[tm] = sr;
                explored[tm] = true;
            }
        });

        getNeighbors(gameModel).forEach(neighbor -> {
            for (int i = 0; i < losRanges.length; i++) {
                int range = neighbor.losRanges[i] - 1;
                if(!passable)
                    range = Math.min(1, range);
                if (losRanges[i] < range) {
                    losRanges[i] = range;
                    explored[i] = true;
                }
            }
        });
    }

    public boolean isVisible(Team team) {
        return losRanges[team.team] != 0;
    }

    public boolean isExplored(Team team) {
        return explored[team.team];
    }

    private boolean isPassableMemo = false;
    private boolean isPassableMemoized = false;
    /**
     * Returns true if the cell is able to be passed through, or equivalently,
     * if a pathing enemy should try to go through this tile. A cell is
     * considered unpassable if it has a cell getHitbox occupying it.
     *
     * @return true if the cell is able to be walked through
     */
    public boolean isPassable() {
        if(isPassableMemoized)
            return isPassableMemo;
        isPassableMemoized = true;
        isPassableMemo = contents.stream().noneMatch(e -> e.getCollideType() == CollisionStrat.CollideType.HARD
                || e.getCollideType() == CollisionStrat.CollideType.WATER);
        return isPassableMemo;
    }

    private boolean isVisibleMemo;
    private boolean isVisibleMemoized;

    public boolean isSeeThrough() {
        if(isVisibleMemoized)
            return isVisibleMemo;
        isVisibleMemoized = true;
        isVisibleMemo = contents.stream().noneMatch(e -> e.getCollideType() == CollisionStrat.CollideType.HARD);
        return isVisibleMemo;
    }

    /**
     * Gets the contents of the cell.
     *
     * @return the current contents of the cell
     */
    public Collection<Entity> getContents() {
        return contents;
    }

    /**
     * Gets the contents of the cell.
     *
     * @return the current contents of the cell
     */
    public Stream<Entity> streamContents() {
        return contents.stream();
    }

    /**
     * Adds the specified entity to this cell.
     *
     * @param o the entity to be added to this cell
     */
    public void addContents(GameModel model, Entity o) {
        contents.add(o);
        isPassableMemoized = false;
        isVisibleMemoized = false;
    }

    /**
     * Removes the specified entity from this cell.
     *
     * @param o the entity to be removed from this cell
     */
    public void removeContents(GameModel model, Entity o) {
        contents.remove(o);
        isPassableMemoized = false;
        isVisibleMemoized = false;
    }

    private static Map<String, Point> TILE_MAP;
    private static Map<Character, List<Character>> matches;
    private static final int TILE_IMAGE_TILE_SIZE = 32;

    static {

        TILE_MAP = new HashMap<>();
        matches = new HashMap<>();

        Scanner input = new Scanner(Tile.class.getResourceAsStream("tile_map.txt"));

        while (input.hasNext()) {
            Point p = new Point(input.nextInt(), input.nextInt());
            if (p.x == -1)
                break;
            input.nextLine();
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < 3; i++)
                str.append(input.nextLine());
            input.nextLine();
            TILE_MAP.put(str.toString(), p);
        }

        matches.put('G', Arrays.asList('G', 'E', 'X', 'L', 'K', '_'));
        matches.put('D', Arrays.asList('D', 'E', 'U', 'L', 'K', '_'));
        matches.put('W', Arrays.asList('W', 'J', 'X', 'U', 'L', '_'));
        matches.put('B', Arrays.asList('B', 'J', 'X', 'U', 'E', '_'));
    }


    /**
     * Draws the tile in a specified cell on the map.
     *
     * @param firstAnimation TODO
     */
    public void draw(PixelWriter writer, GameModel model, boolean firstAnimation) {
        Point maxPoint = new Point(0, 0);
        StringBuilder hashKey = new StringBuilder();

        for (int j = -1; j < 2; j++)
            for (int i = -1; i < 2; i++)
                hashKey.append(model.getTile(x + i, y + j).tupleNum);

        int max = -1;
        for (String key : TILE_MAP.keySet()) {

            if (key.charAt(4) != hashKey.charAt(4)) // check that middle tile matches
                continue;

            int cur = 0;
            Point curPoint = TILE_MAP.get(key);

            for (int l = 0; l < 9; l++)
                if (matches.get(hashKey.charAt(l)).contains(key.charAt(l))) // check that these two are a possible match
                    cur++;

            if (cur > max) {
                max = cur;
                maxPoint = curPoint;
            }
        }

        PixelReader reader = TILE_IMAGE.getPixelReader();
        if (!firstAnimation && this.tile.tupleNum == 'W')
            writer.setPixels(
                    toDrawCoords(x),
                    toDrawCoords(y),
                    TILE_IMAGE_TILE_SIZE,
                    TILE_IMAGE_TILE_SIZE,
                    reader,
                    (maxPoint.x + 10) * TILE_IMAGE_TILE_SIZE,
                    maxPoint.y * TILE_IMAGE_TILE_SIZE);
        else
            writer.setPixels(
                    toDrawCoords(x),
                    toDrawCoords(y),
                    TILE_IMAGE_TILE_SIZE,
                    TILE_IMAGE_TILE_SIZE,
                    reader,
                    maxPoint.x * TILE_IMAGE_TILE_SIZE,
                    maxPoint.y * TILE_IMAGE_TILE_SIZE);


    }

    /**
     * Perform collisions between the current contents of the cell.
     * The collisions are handled based on the entities involved.
     *
     * @param model current model of the game
     */
    public void collideContents(GameModel model) {
        // TODO fix collisions in different cells (2 objects in 2 cells will collide twice)
        for (int i = 0; i < contents.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (i < contents.size() && j < contents.size()) { //Here because elements may be removed during this loop (arrows)
                    Entity a = contents.get(i);
                    Entity b = contents.get(j);

                    // Check if they collide. If they do, perform collisions
                    // on both.
                    if (Hitbox.testCollision(a, b)) {
//                    System.out.println("COLLIDING");
                        a.collision(model, b);
                        b.collision(model, a);
                    }
                }
            }
        }
    }

    /**
     * Gets the current tile type of the cell.
     *
     * @return the current tile type of the cell
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Sets the tile of the cell to the specified tile
     *
     * @param tile the new tile type of the cell
     */
    public void setTile(Tile tile, GameModel model) {
        this.tile = tile;
    }

    /**
     * Gets the x coordinate of the top left of this grid cell.
     *
     * @return the x coordinate of the top left of this grid cell
     */
    public int getTopLeftX() {
        return x;
    }

    /**
     * Gets the y coordinate of the top left of this grid cell.
     *
     * @return the y coordinate of the top left of this grid cell
     */
    public int getTopLeftY() {
        return y;
    }

    /**
     * Gets the x coordinate of the center of this grid cell.
     *
     * @return the x coordinate of the center of this grid cell
     */
    public double getCenterX() {
        return x + 0.5;
    }

    /**
     * Gets the y coordinate of the center of this grid cell.
     *
     * @return the y coordinate of the center of this grid cell
     */
    public double getCenterY() {
        return y + 0.5;
    }

    @Override
    public boolean equals(Object o) {
        GridCell ot = (GridCell) o;
        return x == ot.x && y == ot.y;
    }

    @Override
    public int hashCode() {
        return (int) (0.5 * (x + y) * (x + y + 1)) + y;
    }
}
