package game.model.Game.WorldObject.Shape;

import game.model.Game.GameModel;
import game.model.Game.Grid.GridCell;
import javafx.scene.canvas.GraphicsContext;

import java.util.Collections;
import java.util.Set;

/*
A shape that always take up an entire cell (for blockers)
 */
public class CellShape extends Shape {

    private GridCellReference gridCell;

    public CellShape(int x, int y) {
        this.gridCell = new GridCellReference(x, y);
    }

    @Override
    public double getX() {
        return gridCell.x + 0.5;
    }

    @Override
    public double getY() {
        return gridCell.y + 0.5;
    }

    @Override
    public boolean blocksCell(int x, int y) {
        return false;
        //RYAN TODO
    }

    @Override
    Set<GridCellReference> getCellsReference() {
        return Collections.singleton(gridCell);
    }

    @Override
    public boolean sameCellTestCollision(Shape shape) {
        return true;
    }

    @Override
    public void shift(double x, double y) {
        this.gridCell.x += Math.round(x);
        this.gridCell.y += Math.round(y);

        if ((x - Math.round(x)) > 0.01 || (y - Math.round(y)) > 0.01)
            throw new RuntimeException("Cell shape cannot move by non-integer");
    }

    @Override
    public void setPos(double x, double y) {
        this.gridCell.x = (int) Math.round(x);
        this.gridCell.y = (int) Math.round(y);
        if ((x - Math.round(x)) > 0.01 || (y - Math.round(y)) > 0.01)
            throw new RuntimeException("Cell shape cannot be set to non-integer");
    }

    @Override
    public void rotate(double r) {
        throw new RuntimeException("Cell shape cannot rotate");
    }

    @Override
    public void setAngle(double r) {
        throw new RuntimeException("Cell shape cannot set angle");
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.fillRect(this.gridCell.x * 32, this.gridCell.y * 32, 32, 32);
    }
}
