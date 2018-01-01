import java.util.HashSet;
import java.util.Set;

public abstract class RectangleWorldObject<T extends RectangleWorldObject.RectangleWorldObjectData> extends PositionWorldObject<T> {

    public static class RectangleWorldObjectData <T extends RectangleWorldObject> extends PositionWorldObjectData<T> {
        double w;
        double h;
    }

    @Override
    public void set(T t) {
        super.set(t);
        data.w = t.w;
        data.h = t.h;
    }

    RectangleWorldObject(double x, double y, double w, double h, Model model) {
        super(x, y, model);
        data.w = w;
        data.h = h;
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
    Set<Tile> getTiles(Map map) {
        Set<Tile> set = new HashSet<>();
        for(int i = (int)(data.x - data.w); i <= Math.ceil(data.x + data.w); i++){
            for(int j = (int)(data.y - data.h); j <= Math.ceil(data.y + data.h); j++){
                set.add(map.get(i,j));
            }
        }
        return set;
    }
}
