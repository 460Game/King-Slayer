package game.Pathing;

import com.esotericsoftware.minlog.Log;
import game.model.Game.Grid.GridCell;
import game.model.Game.Model.GameModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.stream.Collectors;

import static Util.Const.TILE_PIXELS;

/**
 * Class used to perform A* search on the graph.
 */
public class Astar {

    private Set<GridCell> nodes;

    private Collection<GridCell> cells;

    private GameModel model;

    public Astar(GameModel model) {
        this.model = model;
        cells = model.getAllCells();
        nodes = new HashSet<>();
    }

    public List<GridCell> astar(GridCell start, GridCell end) {
        Set<GridCell> closed = new HashSet<>();
        Set<GridCell> open = new HashSet<>();
        Map<GridCell, GridCell> prevNode = new HashMap<>();
        Map<GridCell, Double> g = new HashMap<>();
        Map<GridCell, Double> f = new HashMap<>();
        for (GridCell node : nodes) {
            g.put(node, Double.POSITIVE_INFINITY);
            f.put(node, Double.POSITIVE_INFINITY);
        }
//        System.out.println("Traversable nodes : " + nodes.size());
        open.add(start);
        g.put(start, 0.0);
//        f.put(start, heuristicValue(start.getX(), start.getY(), end.getX(), end.getY()));
        f.put(start, heuristicValue(start, end));
        while(!open.isEmpty()) {
//            System.out.println("ANOTHER LOOP");
            GridCell current = start;
            double score = Double.POSITIVE_INFINITY;
            for (GridCell cell : open)
                if (f.get(cell) < score) {
                    current = cell;
                    score = f.get(cell);
                }
            if (current.equals(end)) {
               return getPath(prevNode, current);
            }
            open.remove(current);
            closed.add(current);

            for (int i = Math.max(0, current.getX() - 1); i <= Math.min(current.getX() + 1, model.getMapWidth() - 1); i++) {
                for (int j = Math.max(0, current.getY() - 1); j <= Math.min(current.getY() + 1, model.getMapHeight() - 1); j++) {
//                    System.out.println(" ENTERING neighborLOOP at " + i + ", " + j);
                    GridCell neighbor = model.getCell(i, j);
                    if (!this.nodes.contains(neighbor))
                        continue;
                    if (closed.contains(neighbor))
                        continue;
                    if (!open.contains(neighbor))
                        open.add(neighbor);
                    double tempg = g.get(current) + Util.Util.dist(current.getX(), current.getY(), neighbor.getX(), neighbor.getY());
                    if (tempg >= g.get(neighbor))
                        continue;
//                    System.out.println("Calculated distance");
                    prevNode.put(neighbor, current);
                    g.put(neighbor, tempg);
                    f.put(neighbor, g.get(neighbor) + heuristicValue(neighbor, end));
                }
            }
//            System.out.println("SIZE of open set: " + open.size());
        }
        throw new RuntimeException("A* failed to produce a path.");
        // TODO fix diagonal paths where tiles cannot be passed through
        // TODO fix path through trees
    }

    private List<GridCell> path = null;

    public List<GridCell> getPath(Map<GridCell, GridCell> prevNodes, GridCell current) {
        path = new LinkedList<>();
        path.add(current);
        while (prevNodes.keySet().contains(current)) {
            current = prevNodes.get(current);
            path.add(0, current);
        }
//        System.out.println("Path size: " + path.size());
//        for (GridCell cell : path)
//            System.out.println("Cell x, y: " + cell.getX() + ", " + cell.getY());
        return path;
    }

    public void findTraversableNodes() {
        nodes = new HashSet<>();
        nodes = cells.stream().filter(GridCell::isPassable).collect(Collectors.toSet());
//        for (GridCell node : nodes)
//            System.out.println("Node x,y: " + node.getX() + ", " + node.getY());
//        System.out.println(nodes.size());
    }

    public Set<GridCell> getNodes() {
        return nodes;
    }

    public double heuristicValue(GridCell a, GridCell b) {
//        return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        return (a.getX() - b.getX()) * (a.getX() - b.getX()) + (a.getY() - b.getY()) * (a.getY() - b.getY());
    }

    public void draw(GraphicsContext gc) {
        if(path != null) {
            gc.setLineWidth(5);
            gc.setFill(Color.BLUE);
            gc.strokePolyline( path.stream().mapToDouble(c -> TILE_PIXELS * (c.getX() + 0.5)).toArray() ,path.stream().mapToDouble(c -> TILE_PIXELS * (c.getY() + 1.5)).toArray(), path.size());
        }
    }
}
