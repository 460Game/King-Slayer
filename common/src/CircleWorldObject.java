import java.util.HashSet;
import java.util.Set;

public abstract class CircleWorldObject<T extends CircleWorldObjectData> extends PositionWorldObject<T> {

    double r;

    @Override
    public void set(T t) {
        super.set(t);
        this.r = t.r;
    }

    @Override
    Set<Tile> getTiles(Model model) {
        Set<Tile> set = new HashSet<>();
//TODO
        return set;
    }

    CircleWorldObject(double x, double y, double r, Model model) {
        super(x, y, model);
        this.r = r;
    }

    @Override
    public boolean isRound() {
        return true;
    }

    @Override
    public boolean isRect() {
        return false;
    }
}
