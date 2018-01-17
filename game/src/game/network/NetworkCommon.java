package game.network;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import game.message.Message;
import game.model.Game.GameModel;

public class NetworkCommon {
    static public int port = 54555;
    static public final int REMOTEMODEL = 1;
    // This registers serializes files
    public static void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Message.class);
        kryo.register(StartMsg.class);
    }
    public static class StartMsg implements Message{
        @Override
        public void execute(GameModel model) {

        }
    }
}

