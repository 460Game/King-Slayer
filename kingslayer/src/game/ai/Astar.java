package game.ai;

import game.model.game.grid.GridCell;
import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;
import util.*;
import java.util.stream.Collectors;

import static util.Const.TILE_PIXELS;

/**
 * Class used to find paths for entities in the game map.
 */
public class Astar {

    /**
     * Current model of the game world. Holds the current
     * state of the map with which the search is used on.
     */
    private GameModel model;

    /**
     * Holds the set of passable cells in the current
     * state of the map.
     */
    private Set<GridCell> passable;

    /**
     * Collection of cells that make up the game map. The
     * contents of each will aid in the path finding.
     */
    private Collection<GridCell> cells;

    /**
     * List of cells that make up the path that was found. First cell
     * should be the start cell, and last cell should be the end cell.
     */
    private List<GridCell> path = null;

    /**
     * Constructor for the A* pathfinder, given the current
     * model of the game world.
     * @param model current state of the game world
     */
    public Astar(GameModel model) {
        this.model = model;
        cells = model.getAllCells();
        passable = new HashSet<>();
        findTraversableNodes();
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
     */
    public void findTraversableNodes() {
        passable = cells.stream().filter(GridCell::isPassable).collect(Collectors.toSet());
//        for (GridCell node : passable)
//            System.out.println("Node x,y: " + node.getTopLeftX() + ", " + node.getTopLeftY());
//        System.out.println(passable.size());
    }

    /**
     * Calculates the heuristic from getting from grid cell a to grid cell b. The heuristic
     * is the Euclidean distance, without the square root, from the top left corners of the grid cells.
     * @param a starting grid cell
     * @param b destination grid cell
     * @return the heuristic from getting from cell a to cell b
     */
    public double heuristicValue(GridCell a, GridCell b) {
        return Math.sqrt((a.getTopLeftX() - b.getTopLeftX()) * (a.getTopLeftX() - b.getTopLeftX()) +
                (a.getTopLeftY() - b.getTopLeftY()) * (a.getTopLeftY() - b.getTopLeftY()));

        // CHange this to diagonal distance ?
        // dx = abs(node.x - goal.x)
        //dy = abs(node.y - goal.y)
        // return D * (dx + dy) + (D2 - 2 * D) * min(dx, dy)
        // D = 1, D2 = sqrt(2)
    }

    public GridCell findClosestPassable(GridCell cell) {
        return passable.parallelStream().min((c1, c2) -> Double.compare(Util.dist(cell.getCenterX(), cell.getCenterY(), c1.getCenterX(), c1.getCenterY()),
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
        // TODO may need to include finding the traversable nodes
//        findTraversableNodes();
        GridCell c = findClosestPassable(end);
//        System.out.println("End coords: " + end.getTopLeftX() + ", " + end.getTopLeftY());
//        System.out.println("Closest coords: " + c.getTopLeftX() + ", " + c.getTopLeftY());

        if (!passable.contains(end))
            throw new RuntimeException("Destination cell is not passable.");
        // TODO may just pick a cell nearby.

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
        for (GridCell node : passable) {
            g.put(node, Double.POSITIVE_INFINITY);
            f.put(node, Double.POSITIVE_INFINITY);
        }

//        System.out.println("Traversable passable : " + passable.size());

        // G-score of the start cell is 0. F-score of the start cell
        // is all heuristic. Add the start cell to the discovered
        // cell set.
        g.put(start, 0.0);
        f.put(start, heuristicValue(start, c));
        open.add(start);

        // Loop until the set of discovered, but not computed cells is
        // empty.
        while(!open.isEmpty()) {
//            System.out.println("ANOTHER LOOP");

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
            if (current.equals(c)) {
               return getPath(prevCell, current);
            }

            // Remove this cell from the open set, and add it to the
            // closed set.
            open.remove(current);
            closed.add(current);

            // Look at all neighbors of the current cell, checking for the boundaries of the map.
            for (int i = Math.max(0, current.getTopLeftX() - 1); i <= Math.min(current.getTopLeftX() + 1, model.getMapWidth() - 1); i++) {
                for (int j = Math.max(0, current.getTopLeftY() - 1); j <= Math.min(current.getTopLeftY() + 1, model.getMapHeight() - 1); j++) {
//                    System.out.println(" ENTERING neighborLOOP at " + i + ", " + j);

                    // Check if the diagonals are passable.
                    if (!model.getCell(i, current.getTopLeftY()).isPassable() &&
                            !model.getCell(current.getTopLeftX(), j).isPassable())
                        continue;
                    // TODO may need to check for edge cases.

                    GridCell neighbor = model.getCell(i, j);

                    // If the neighbor is not passable, cannot be in the path.
                    if (!this.passable.contains(neighbor))
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

//                    System.out.println("Calculated distance");

                    // Update the mappings of previous cells, g-scores, and f-scores.
                    prevCell.put(neighbor, current);
                    g.put(neighbor, tempg);
                    f.put(neighbor, g.get(neighbor) + heuristicValue(neighbor, c));
                }
            }
//            System.out.println("SIZE of open set: " + open.size());
        }

        // A* couldn't find a path due to obstacles
        throw new RuntimeException("A* failed to produce a path.");
        // TODO fix diagonal paths where tiles cannot be passed through
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

        // While the current cell has a preceding cell in the path, update the current cell
        // add it to the front of the list.
        while (prevCells.keySet().contains(current)) {
            current = prevCells.get(current);
            path.add(0, current);
        }

//        System.out.println("Path size: " + path.size());
//        for (GridCell cell : path)
//            System.out.println("Cell x, y: " + cell.getTopLeftX() + ", " + cell.getTopLeftY());
        return path;
    }

    /**
     * Finds a path using A* search given a starting cell and an ending cell. This path
     * is stored in the "path" field.
     * @param start the starting cell of the path
     * @param end the destination cell of the path
     */
    public void findPath(GridCell start, GridCell end) {
        findTraversableNodes();
        path = astar(start, end);
    }

    public void moveToCell(Entity e, GridCell cell) {
        e.data.updateData.velocity.setAngle(Math.atan2(cell.getCenterY() - e.data.y, cell.getCenterX() - e.data.x));
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
     * @param gc graphics context used to update the path
     */
    public void draw(GraphicsContext gc) {
        if(path != null) {
            gc.setLineWidth(5);
            gc.setFill(Color.BLUE);
            gc.strokePolyline(path.stream().mapToDouble(c -> TILE_PIXELS * (c.getTopLeftX() + 0.5)).toArray(),
                    path.stream().mapToDouble(c -> TILE_PIXELS * (c.getTopLeftY() + 0.5)).toArray(), path.size());
        }
    }
}
