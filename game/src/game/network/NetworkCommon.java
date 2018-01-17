package game.network;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class NetworkCommon {
    static public int port = 54555;

    // This registers serializes files
    public static void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();

    }
}

