package game.Pathing;

import game.model.Game.Grid.GridCell;
import game.model.Game.Model.GameModel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    }

    public static void astar(double startx, double starty, double endx, double endy) {

    }

    public void findTraversableNodes() {
        nodes = cells.stream().filter(GridCell::isPassable).collect(Collectors.toSet());
        for (GridCell node : nodes)
            System.out.println("Node x,y: " + node.getX() + ", " + node.getY());
        System.out.println(nodes.size());
    }
}
