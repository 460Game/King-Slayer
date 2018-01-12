import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
public class ChatCommon {
    static public int port = 54555;

    // This registers serializes files
    public static void register (EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(UserName.class);
        kryo.register(String[].class);
        kryo.register(AllUserNames.class);
        kryo.register(ChatMsg.class);
    }

    public static class UserName {
        public String user_name;
        public UserName() {}
        public UserName(String name) {
            user_name = name;
        }
    }

    static public class AllUserNames {
        public String[] names;
        public AllUserNames() {}
        public AllUserNames(String[] usrNames) {
            names = usrNames;
        }
    }

    static public class ChatMsg {
        public String msg;
        public ChatMsg() {}
        public ChatMsg(String message) {
            msg = message;
        }
    }
}
