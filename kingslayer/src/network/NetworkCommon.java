package network;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.minlog.Log;
import game.message.*;
import game.message.toClient.*;
import game.message.toServer.*;
import game.model.game.map.Tile;
import game.model.game.model.team.Role;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.team.TeamRoleEntityMap;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntityData;
import game.model.game.model.worldObject.entity.aiStrat.AIDoNothingStrat;
import game.model.game.model.worldObject.entity.collideStrat.GhostCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.HardCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.SoftCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.UnitCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.*;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.drawStrat.*;
import game.model.game.model.worldObject.entity.entities.Velocity;
import game.model.game.model.worldObject.entity.updateStrat.MovingStrat;
import game.model.game.model.worldObject.entity.updateStrat.StillStrat;
import game.model.game.model.worldObject.entity.updateStrat.UpdateData;
import game.model.game.model.worldObject.entity.updateStrat.UpdateStrat;
import lobby.RoleChoice;
import lobby.TeamChoice;

import java.util.ArrayList;
import java.util.HashMap;

public class NetworkCommon {
    static {
        Log.set(Log.LEVEL_INFO);
    }

    public static int port = 54555;

    // This registers serializes files
    public static void register (EndPoint endPoint) {
        KyroRegister(endPoint.getKryo());
    }

    public static void KyroRegister(Kryo kryo) {
        kryo.register(Message.class);
        kryo.register(ActionMessage.class);
        kryo.register(GoDirectionMessage.class);
        kryo.register(RemoveEntityMessage.class);
        kryo.register(DeleteEntityMessage.class);
        kryo.register(MakeEntityMessage.class);
        kryo.register(SetEntityMessage.class);
        kryo.register(SetTileMessage.class);
        kryo.register(ToClientMessage.class);
        kryo.register(ToServerMessage.class);
        kryo.register(Hitbox.class);
        kryo.register(CellHitbox.class);
        kryo.register(CircleHitbox.class);
        kryo.register(CompositeHitbox.class);
        kryo.register(RectHitbox.class);
        kryo.register(Team.class);
        kryo.register(Tile.class);
        kryo.register(ClientMakeModelMsg.class);
        kryo.register(ClientReadyMsg.class);
        kryo.register(RemoteConnection.GameConnection.class);
        kryo.register(ClientStartModelMsg.class);
        kryo.register(ArrayList.class);
        kryo.register(SyncClockMsg.class);
        kryo.register(EntityData.class);
        kryo.register(UpdateData.class);
        kryo.register(Velocity.class);
        kryo.register(InitGameMessage.class);
        kryo.register(UpdateResourcesMessage.class);
        kryo.register(TeamResourceData.class);
        kryo.register(RequestEntityMessage.class);
        kryo.register(NewEntityMessage.class);
        kryo.register(Entity.class);
        kryo.register(AIDoNothingStrat.class);


        kryo.register(ImageDrawStrat.WallImageDrawStrat.class);
        kryo.register(ImageDrawStrat.TreeImageDrawStrat.class);
        kryo.register(ImageDrawStrat.StoneImageDrawStrat.class);
        kryo.register(ImageDrawStrat.MetalImageDrawStrat.class);
        kryo.register(ImageDrawStrat.BoxImageDrawStrat.class);
        kryo.register(ImageDrawStrat.WallImageDrawStrat.class);
        kryo.register(ImageDrawStrat.WallBuildableImageDrawStrat.class);
        kryo.register(ImageDrawStrat.RedResourceCollectorImageDrawStrat.class);
        kryo.register(UnitCollisionStrat.class);
        kryo.register(SoftCollisionStrat.class);
        kryo.register(HardCollisionStrat.class);
        kryo.register(GhostCollisionStrat.class);
        kryo.register(DirectionAnimationDrawStrat.RedSlayerDirectionAnimationDrawStrat.class);
        kryo.register(DirectionAnimationDrawStrat.RedKingDirectionAnimationDrawStrat.class);
        kryo.register(DirectionAnimationDrawStrat.BlueKingDirectionAnimationDrawStrat.class);
        kryo.register(DirectionAnimationDrawStrat.BlueSlayerDirectionAnimationDrawStrat.class);

        kryo.register(DrawData.class);
        kryo.register(AnimateDrawStrat.class);
        kryo.register(DrawStrat.class);
        kryo.register(ImageDrawStrat.class);
        kryo.register(NoDrawStrat.class);
        kryo.register(ShapeDrawStrat.class);
        kryo.register(MovingStrat.class);
        kryo.register(StillStrat.class);
        kryo.register(UpdateData.class);
        kryo.register(UpdateStrat.class);
        kryo.register(StopMessage.class);
        kryo.register(InitGameMessage.class);
        kryo.register(NewEntityMessage.class);
        kryo.register(RemoveEntityMessage.class);
        kryo.register(SetEntityMessage.class);
        kryo.register(SetTileMessage.class);
        kryo.register(ToClientMessage.class);
        kryo.register(UpdateResourcesMessage.class);
        kryo.register(ActionMessage.class);
        kryo.register(GoDirectionMessage.class);

        kryo.register(TeamRoleEntityMap.class);
        kryo.register(long[][].class);

        kryo.register(long[].class);
        kryo.register(Role.class);
        kryo.register(Team.class);
        kryo.register(AllClientConnectMsg.class);
        kryo.register(HashMap.class);
        kryo.register(GhostCollisionStrat.class);
        kryo.register(TeamResourceData.Resource.class);
        kryo.register(ImageDrawStrat.TreasureImageDrawStrat.class);
//        kryo.register(GhostCollisionStrat.class);
//        kryo.register(GhostCollisionStrat.class);
//        kryo.register(GhostCollisionStrat.class);
//        kryo.register(GhostCollisionStrat.class);

    }

    public static class ClientMakeModelMsg {
        public ClientMakeModelMsg() {
        }
    }

    public static class ClientReadyMsg {
        private Team teamChoice;
        private Role roleChoice;
        public ClientReadyMsg() {}
        public ClientReadyMsg(Team team, Role role) {
            teamChoice = team;
            roleChoice = role;
        }
        public Team getTeam() {
            return teamChoice;
        }
        public Role getRole() {
            return roleChoice;
        }
    }

    public static class ClientStartModelMsg {
        public ClientStartModelMsg() {}
    }

    public static class AllClientConnectMsg {
        public AllClientConnectMsg() {}
    }

    public static class SyncClockMsg {
        private long serverTime;
        public SyncClockMsg() {}
        public SyncClockMsg(long serverNanoTime) {
            serverTime = serverNanoTime;
        }
        public long getServerTime() {
            return serverTime;
        }
    }

//    public static class BatchMsg {
//        ArrayList<Message> msgs;
//        public BatchMsg() {}
//        public BatchMsg(ArrayList<Message> messages) {
//            msgs = messages;
//        }
//        public ArrayList<Message> getMsgs() {
//            return msgs;
//        }
//    }

}

