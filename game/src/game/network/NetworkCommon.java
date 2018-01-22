package game.network;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import game.message.*;
import game.message.playerMoveMessage.*;
import game.model.Game.Map.Tile;
import game.model.Game.WorldObject.Entity.Blocker;
import game.model.Game.WorldObject.Entity.Entity;
import game.model.Game.WorldObject.Entity.Tree;
import game.model.Game.WorldObject.Shape.*;
import game.model.Game.WorldObject.Entity.TestPlayer;
import game.model.Game.WorldObject.Shape.Shape;
import game.model.Game.WorldObject.Team;

import java.awt.*;

public class NetworkCommon {
    static public int port = 54555;
    static public final int REMOTEMODEL = 1;
    // This registers serializes files
    public static void register (EndPoint endPoint) {
        KyroRegister(endPoint.getKryo());
    }

    public static void KyroRegister(Kryo kryo) {
        kryo.register(Message.class);
        kryo.register(PlayerDown.class);
        kryo.register(PlayerLeft.class);
        kryo.register(PlayerRight.class);
        kryo.register(PlayerStopHorz.class);
        kryo.register(PlayerStopVert.class);
        kryo.register(PlayerUp.class);
        kryo.register(ActionMessage.class);
        kryo.register(RemoveEntityMessage.class);
        kryo.register(SetEntityMessage.class);
        kryo.register(SetTileMessage.class);
        kryo.register(ToClientMessage.class);
        kryo.register(ToServerMessage.class);
        kryo.register(SetPlayerMessage.class);

        kryo.register(TestPlayer.class);
        kryo.register(Blocker.class);
        kryo.register(Entity.class);
        kryo.register(Tree.class);
        kryo.register(Shape.GridCellReference.class);

        kryo.register(Shape.class);
        kryo.register(CellShape.class);
        kryo.register(CircleShape.class);
        kryo.register(CompositeShape.class);
        kryo.register(RectShape.class);

        kryo.register(Team.class);

        kryo.register(Tile.class);



        kryo.register(ClientMakeModelMsg.class);
        kryo.register(ClientReadyMsg.class);

        kryo.register(RemoteConnection.GameConnection.class);
        kryo.register(ClientStartModelMsg.class);

    }

    public static class ClientMakeModelMsg {
        public ClientMakeModelMsg() {
        }
    }

    public static class ClientReadyMsg {
        public ClientReadyMsg() {}
    }

    public static class ClientStartModelMsg {
        public ClientStartModelMsg() {}
    }

}

