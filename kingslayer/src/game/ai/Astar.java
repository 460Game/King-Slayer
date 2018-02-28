package game.ai;

import com.esotericsoftware.minlog.Log;
import game.model.game.grid.GridCell;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;
import util.*;
import java.util.stream.Collectors;

import static util.Const.TILE_PIXELS;

/**
 * Class used for AI, specifically for finding paths for entities
 * in the game map.
 */
public class Astar {

    /**
     * Current model of the game world. Holds the current state of the map with
     * which the search is used on. This class is used for AI planning, which is
     * only computed on the server.
     */
    private ServerGameModel model;

    /**
     * Holds the set of passable cells in the current state of the map.
     */
    private Set<GridCell> passable;

    /**
     * Collection of cells that make up the game map. The contents of each
     * will aid in the path finding.
     */
    private Collection<GridCell> cells;

    /**
     * List of cells that make up the path that was found. First cell
     * should be the start cell, and last cell should be the end cell.
     */
    private List<GridCell> path;

    /**
     * Constructor for the A* pathfinder, given the current
     * model of the game world.
     * @param model current state of the game world
     */
    public Astar(ServerGameModel model) {
        this.model = model;
        cells = model.getAllCells();
        passable = findTraversableCells();
        path = new LinkedList<>();
    }

    /**
     * Get the set of passable nodes in the current model.
     * @return the set of passable nodes
     */
    public Set<GridCell> getPassable() {
        return passable;
    }

    /**
     * Finds all of the passable nodes in the map and stores them
     * in passable.
     * @return list of cells that are passable
     */
    public Set<GridCell> findTraversableCells() {
        return cells.stream().filter(GridCell::isPassable).collect(Collectors.toSet());
    }

    /**
     * Calculates the heuristic from getting from grid cell a to grid cell b. The heuristic
     * is the Euclidean distance, without the square root, from the top left corners of the grid cells.
     * @param a starting grid cell
     * @param b destination grid cell
     * @return the heuristic from getting from cell a to cell b
     */
    private double heuristicValue(GridCell a, GridCell b) {
        return Math.sqrt((a.getTopLeftX() - b.getTopLeftX()) * (a.getTopLeftX() - b.getTopLeftX()) +
                (a.getTopLeftY() - b.getTopLeftY()) * (a.getTopLeftY() - b.getTopLeftY()));

        // Change this to diagonal distance ?
        // dx = abs(node.x - goal.x)
        //dy = abs(node.y - goal.y)
        // return D * (dx + dy) + (D2 - 2 * D) * min(dx, dy)
        // D = 1, D2 = sqrt(2)
    }

    /**
     * Gets the closest passable tile given a cell.
     * @param cell cell to find the closest passable tile from
     * @return closest passable tile to the specified cell
     */
    public GridCell findClosestPassable(GridCell cell) {
        return passable.parallelStream().min((c1, c2) ->
                Double.compare(Util.dist(cell.getCenterX(), cell.getCenterY(), c1.getCenterX(), c1.getCenterY()),
                Util.dist(cell.getCenterX(), cell.getCenterY(), c2.getCenterX(), c2.getCenterY()))).get();
    }

    /**
     * Perform the A* search given a starting cell and an ending cell. The resulting
     * path goes from adjacent cell, where adjacent is any of the 8 directions.
     * The path should not go through two cells diagonally if two blocking
     * cells are touching the corner in between the diagonal.
     * @param start the starting cell of the path
     * @param end the destination cell of the path
     * @return a list of grid cells describing the path from the start to the end
     */
    public List<GridCell> astar(GridCell start, GridCell end) {
        // Set of cells already considered.
        Set<GridCell> closed = new HashSet<>();

        // Set of cells found but not yet finished computing.
        Set<GridCell> open = new HashSet<>();

        // Maps each cell to the cell that precedes it in the path.
        // Used to traceback and get the actual path. Should hold
        // the most efficient previous cell.
        Map<GridCell, GridCell> prevCell = new HashMap<>();

        // Maps each cell to its g-score and f-score. G-score holds
        // the distance from the start cell to the current cell. F-score
        // holds the distance from the start cell to the end cell passing
        // through the current cell.
        Map<GridCell, Double> g = new HashMap<>();
        Map<GridCell, Double> f = new HashMap<>();

        // Scores are set to infinity for all the cells in the beginning.
        for (GridCell cell : passable) {
            g.put(cell, Double.POSITIVE_INFINITY);
            f.put(cell, Double.POSITIVE_INFINITY);
        }

        // This is here in case the end is not passable. In this case, we just
        // want the path to end at the destination.
        g.put(end, Double.POSITIVE_INFINITY);
        f.put(end, Double.POSITIVE_INFINITY);

        // G-score of the start cell is 0. F-score of the start cell
        // is all heuristic. Add the start cell to the discovered
        // cell set.
        g.put(start, 0.0);
        f.put(start, heuristicValue(start, end));
        open.add(start);

        // Loop until the set of discovered, but not computed cells is
        // empty.
        while(!open.isEmpty()) {
            // Temporary variables.
            GridCell current = start;
            double score = Double.POSITIVE_INFINITY;

            // Find the cell in the open set that has the lowest f-score.
            for (GridCell cell : open)
                if (f.get(cell) < score) {
                    current = cell;
                    score = f.get(cell);
                }

            // If the cell equals the destination cell, we are done.
            if (current.equals(end)) {
               return getPath(prevCell, current);
            }

            // Remove this cell from the open set, and add it to the
            // closed set.
            open.remove(current);
            closed.add(current);

            // Look at all neighbors of the current cell, checking for the boundaries of the map.
            for (int i = Math.max(0, current.getTopLeftX() - 1); i <= Math.min(current.getTopLeftX() + 1, model.getMapWidth() - 1); i++) {
                for (int j = Math.max(0, current.getTopLeftY() - 1); j <= Math.min(current.getTopLeftY() + 1, model.getMapHeight() - 1); j++) {

                    // Check if the diagonals are passable.
                    if (!model.getCell(i, current.getTopLeftY()).isPassable() &&
                            !model.getCell(current.getTopLeftX(), j).isPassable())
                        continue;

                    GridCell neighbor = model.getCell(i, j);

                    // If the neighbor is not passable, cannot be in the path unless this neighbor
                    // is the final cell.
                    if (!this.passable.contains(neighbor) && !neighbor.equals(end))
                        continue;

                    // If the neighbor was already looked at, ignore it.
                    if (closed.contains(neighbor))
                        continue;

                    // Add the neighbor to the open set if not already in it.
                    if (!open.contains(neighbor))
                        open.add(neighbor);

                    // Calculate a g-score if the neighbor were to be passed through from the current cell.
                    double tempg = g.get(current) + Util.dist(current.getTopLeftX(), current.getTopLeftY(),
                            neighbor.getTopLeftX(), neighbor.getTopLeftY());

                    // If the calculated g-score is less than the g-score already found for the neighbor cell,
                    // we ignore this potential path.
                    if (tempg >= g.get(neighbor))
                        continue;

                    // Update the mappings of previous cells, g-scores, and f-scores.
                    prevCell.put(neighbor, current);
                    g.put(neighbor, tempg);
                    f.put(neighbor, g.get(neighbor) + heuristicValue(neighbor, end));
                }
            }
        }

        // A* couldn't find a path due to obstacles.
        return new LinkedList<>();
    }

    /**
     * Gets the path by backtracking from a map of cell to the previous cell in the path.
     * Current cell should always equal the destination of the path.
     * @param prevCells mapping from a cell to the preceding cell in the path
     * @param current the final cell in the path
     * @return the list of grid cells that constitute the path from the start cell to the end cell
     */
    public List<GridCell> getPath(Map<GridCell, GridCell> prevCells, GridCell current) {
        path = new LinkedList<>();

        // Add the final cell in the path.
        path.add(current);

        // While the current cell has a preceding cell in the path, draw the current cell
        // add it to the front of the list.
        while (prevCells.keySet().contains(current)) {
            current = prevCells.get(current);
            path.add(0, current);
        }
        return path;
    }

    /**
     * Moves the entity to the specified cell. This is done by simply
     * setting the entity's direction to move to that cell.
     * @param e entity to move
     * @param cell destination cell
     */
    public void moveToCell(Entity e, GridCell cell) {
       e.setVelocity(e.getVelocity().withAngle(Math.atan2(cell.getCenterY() - e.getY(), cell.getCenterX() - e.getX())));
    }

    /**
     * Moves the entity to the specified point. This is done by simply
     * setting the entity's direction to move to that point.
     * @param e entity to move
     * @param x x-coordinate of destination
     * @param y y-coordinate of destination
     */
    public void moveToPoint(Entity e, double x, double y) {
        e.setVelocity(e.getVelocity().withAngle(Math.atan2(y - e.getY(), x - e.getX())));
    }

    /**
     * Gets the path that this search found.
     * @return the path that this search found
     */
    public List<GridCell> pathGetter() {
        return path;
    }

    /**
     * Draw the path that the search found.
     * @param gc graphics context used to draw the path
     */
    public void draw(GraphicsContext gc) {
        if(path != null) {
            gc.setLineWidth(5);
            gc.setFill(Color.BLUE);
            gc.strokePolyline(path.stream().mapToDouble(c -> TILE_PIXELS * (c.getTopLeftX() + 0.5)).toArray(),
                    path.stream().mapToDouble(c -> TILE_PIXELS * (c.getTopLeftY() + 0.5)).toArray(), path.size());
        }
    }

    /**
     * Updates the model that this class uses to find paths. This
     * should be called whenever an entity is built or destroyed
     * that may affect path planning.
     * @param model most up-to-date state of the model
     */
    public void updateModel(ServerGameModel model) {
        this.model = model;
        cells = model.getAllCells();
        passable = findTraversableCells();
    }

    /**
     * Gets the closest wood tile given a cell.
     * @param cell cell to find the closest wood tile from
     * @return closest wood tile to the specified cell
     */
    public GridCell getClosestWood(GridCell cell) {
        // TODO make more efficient
        return model.getWood().parallelStream().min((c1, c2) ->
                Double.compare(Util.dist(cell.getCenterX(), cell.getCenterY(), c1.getCenterX(), c1.getCenterY()),
                Util.dist(cell.getCenterX(), cell.getCenterY(), c2.getCenterX(), c2.getCenterY()))).get();
    }

    /**
     * Gets the closest stone tile given a cell.
     * @param cell cell to find the closest stone tile from
     * @return closest stone tile to the specified cell
     */
    public GridCell getClosestStone(GridCell cell) {
        return model.getStone().parallelStream().min((c1, c2) ->
                Double.compare(Util.dist(cell.getCenterX(), cell.getCenterY(), c1.getCenterX(), c1.getCenterY()),
                        Util.dist(cell.getCenterX(), cell.getCenterY(), c2.getCenterX(), c2.getCenterY()))).get();
    }

    /**
     * Gets the closest metal tile given a cell.
     * @param cell cell to find the closest metal tile from
     * @return closest metal tile to the specified cell
     */
    public GridCell getClosestMetal(GridCell cell) {
        return model.getMetal().parallelStream().min((c1, c2) ->
                Double.compare(Util.dist(cell.getCenterX(), cell.getCenterY(), c1.getCenterX(), c1.getCenterY()),
                        Util.dist(cell.getCenterX(), cell.getCenterY(), c2.getCenterX(), c2.getCenterY()))).get();
    }

    /**
     * Gets the closest collector tile for a certain team, given a cell.
     * @param cell cell to find the closest collector tile from
     * @param team closest collector belonging to this team to look for
     * @return closest collector tile to the specified cell and team
     */
    public GridCell getClosestCollector(GridCell cell, Team team) {
        if (team == Team.ONE) {
            if (model.getTeam1collector().isEmpty())
                return null; // TODO temp
            return model.getTeam1collector().parallelStream().min((c1, c2) ->
                    Double.compare(Util.dist(cell.getCenterX(), cell.getCenterY(), c1.getCenterX(), c1.getCenterY()),
                            Util.dist(cell.getCenterX(), cell.getCenterY(), c2.getCenterX(), c2.getCenterY()))).get();
        } else {
            if (model.getTeam2collector().isEmpty())
                return null; // TODO temp
            return model.getTeam2collector().parallelStream().min((c1, c2) ->
                    Double.compare(Util.dist(cell.getCenterX(), cell.getCenterY(), c1.getCenterX(), c1.getCenterY()),
                            Util.dist(cell.getCenterX(), cell.getCenterY(), c2.getCenterX(), c2.getCenterY()))).get();
            // TODO support multiple teams
        }
    }
}
