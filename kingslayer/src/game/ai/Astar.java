package game.ai;

import game.model.game.grid.GridCell;
import game.model.game.model.GameModel;
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
     *
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
        passable = cells.stream().filter(cell -> cell.isPassable(model)).collect(Collectors.toSet());
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
        return (a.getTopLeftX() - b.getTopLeftX()) * (a.getTopLeftX() - b.getTopLeftX()) +
                (a.getTopLeftY() - b.getTopLeftY()) * (a.getTopLeftY() - b.getTopLeftY());
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
        f.put(start, heuristicValue(start, end));
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
//                    System.out.println(" ENTERING neighborLOOP at " + i + ", " + j);

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
                    double tempg = g.get(current) + Util.dist(current.getTopLeftX(), current.getTopLeftY(), neighbor.getTopLeftX(), neighbor.getTopLeftY());

                    // If the calculated g-score is less than the g-score already found for the neighbor cell,
                    // we ignore this potential path.
                    if (tempg >= g.get(neighbor))
                        continue;

//                    System.out.println("Calculated distance");

                    // Update the mappings of previous cells, g-scores, and f-scores.
                    prevCell.put(neighbor, current);
                    g.put(neighbor, tempg);
                    f.put(neighbor, g.get(neighbor) + heuristicValue(neighbor, end));
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
     * Draw the path that the search found.
     * @param gc graphics context used to draw the path
     */
    public void draw(GraphicsContext gc) {
        if(path != null) {
            gc.setLineWidth(5);
            gc.setFill(Color.BLUE);
            gc.strokePolyline(path.stream().mapToDouble(c -> TILE_PIXELS * (c.getTopLeftX() + 0.5)).toArray(),
                    path.stream().mapToDouble(c -> TILE_PIXELS * (c.getTopLeftY() + 1.5)).toArray(), path.size());
        }
    }
}
