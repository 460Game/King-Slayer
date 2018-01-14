import Command.ActionMessage;
import Command.UpdateMessage;
import Entity.CollidingWorldObject;
import Entity.Player;
import Entity.WorldObject;
import Model.GameMap;
import Model.GameModel;
import Model.WorldClock;
import Shape.Shape;
import Tile.Tile;
import Util.Util;
import client.Drawable;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class NetWorkCommon {
    static public int port = 54555;

    // This registers serializes files
    public static void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

        kryo.register(GameMap.class);
        kryo.register(UpdateMessage.class);
        kryo.register(ActionMessage.class);
        kryo.register(Shape.CirlceShape.class);
        kryo.register(CollidingWorldObject.class);
        kryo.register(Shape.CompositeShape.class);
        kryo.register(Drawable.class);
        kryo.register(GameModel.class);
        kryo.register(Player.class);
        kryo.register(Shape.RectShape.class);
        kryo.register(Tile.class);
        kryo.register(Tile.TileBridge.class);
        kryo.register(Tile.TilePassable.class);
        kryo.register(Tile.TileTresure.class);
        kryo.register(Util.class);
        kryo.register(WorldClock.class);
        kryo.register(WorldObject.class);
    }
}
