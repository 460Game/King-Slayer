import java.util.HashSet;
import java.util.Set;

public abstract class CircleWorldObject<T extends CircleWorldObject.CircleWorldObjectData> extends PositionWorldObject<T> {

    public static class CircleWorldObjectData <T extends CircleWorldObject> extends PositionWorldObjectData<T> {
        double r;
    }

    @Override
    Set<Tile> getTiles(Map map) {
        Set<Tile> set = new HashSet<>();
        for(int i = (int)(data.x - data.r); i <= Math.ceil(data.x + data.r); i++){
            for(int j = (int)(data.y - data.r); j <= Math.ceil(data.y + data.r); j++){
                if((i+0.5-data.x)*(i+0.5-data.x) + (j+0.5-data.y)*(j+0.5-data.y) <= (data.r+0.5)*(data.r+0.5))
                    set.add(map.get(i,j));
            }
        }
        return set;
    }

    CircleWorldObject(double x, double y, double r, GameModel model) {
        super(x, y, model);
        data.r = r;
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
