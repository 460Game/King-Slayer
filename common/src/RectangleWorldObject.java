import java.util.HashSet;
import java.util.Set;

public abstract class RectangleWorldObject<T extends RectangleWorldObjectData> extends PositionWorldObject<T> {

    double w;
    double h;

    @Override
    public void set(T t) {
        super.set(t);
        this.w = t.w;
        this.h = t.h;
    }

    RectangleWorldObject(double x, double y, double w, double h, Model model) {
        super(x, y, model);
        this.w = w;
        this.h = h;
    }

    @Override
    public boolean isRound() {
        return false;
    }

    @Override
    public boolean isRect() {
        return true;
    }

    @Override
    Set<Tile> getTiles(Model model) {
        Set<Tile> set = new HashSet<>();
    //TODO
        return set;
    }
}
