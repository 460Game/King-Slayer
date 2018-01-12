import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class NetWorkCommon {
    static public int port = 54555;

    // This registers serializes files
    public static void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        kryo.register(Map.class);
        kryo.register(UpdateCommand.class);
        kryo.register(ActionCommand.class);
        kryo.register(CirlceShape.class);
        kryo.register(CollidingWorldObject.class);
        kryo.register(CompositeShape.class);
        kryo.register(Drawable.class);
        kryo.register(GameModel.class);
        kryo.register(Player.class);
        kryo.register(RectShape.class);
        kryo.register(Tile.class);
        kryo.register(TileBridge.class);
        kryo.register(TilePassable.class);
        kryo.register(TileTresure.class);
        kryo.register(Util.class);
        kryo.register(WorldClock.class);
        kryo.register(WorldObject.class);
    }
}
