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
import game.model.game.model.worldObject.entity.collideStrat.*;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.*;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.deathStrat.KingDeathStrat;
import game.model.game.model.worldObject.entity.deathStrat.NopDeathStrat;
import game.model.game.model.worldObject.entity.deathStrat.SlayerDeathStrat;
import game.model.game.model.worldObject.entity.drawStrat.*;
import game.model.game.model.worldObject.entity.entities.Velocity;
import game.model.game.model.worldObject.entity.updateStrat.*;

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
        kryo.register(ActionRequest.class);
        kryo.register(GoDirectionRequest.class);
        kryo.register(RemoveEntityCommand.class);
        kryo.register(EntityBuildRequest.class);
        kryo.register(SetEntityCommand.class);
        kryo.register(SetTileCommand.class);
        kryo.register(ToClientCommand.class);
        kryo.register(ToServerRequest.class);
        kryo.register(ShootArrowRequest.class);
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
        kryo.register(InitGameCommand.class);
        kryo.register(UpdateResourceCommand.class);
        kryo.register(TeamResourceData.class);
        kryo.register(RequestEntityRequest.class);
        kryo.register(NewEntityCommand.class);
        kryo.register(Entity.class);
        kryo.register(AIDoNothingStrat.class);


        kryo.register(ImageDrawStrat.WallImageDrawStrat.class);
        kryo.register(ImageDrawStrat.TreeImageDrawStrat.class);
        kryo.register(ImageDrawStrat.StoneImageDrawStrat.class);
        kryo.register(ImageDrawStrat.MetalImageDrawStrat.class);
        kryo.register(ImageDrawStrat.BoxImageDrawStrat.class);
        kryo.register(ImageDrawStrat.WallImageDrawStrat.class);
        kryo.register(ImageDrawStrat.WallBuildableImageDrawStrat.class);
        kryo.register(UpgradableImageDrawStrat.RedResourceCollectorImageDrawStrat.class);
        kryo.register(UpgradableImageDrawStrat.BlueResourceCollectorImageDrawStrat.class);
        kryo.register(UnitCollisionStrat.class);
        kryo.register(SoftCollisionStrat.class);
        kryo.register(HardCollisionStrat.class);
        kryo.register(GhostCollisionStrat.class);
        kryo.register(DirectionAnimationDrawStrat.RedSlayerDirectionAnimationDrawStrat.class);
        kryo.register(DirectionAnimationDrawStrat.RedKingDirectionAnimationDrawStrat.class);
        kryo.register(DirectionAnimationDrawStrat.BlueKingDirectionAnimationDrawStrat.class);
        kryo.register(DirectionAnimationDrawStrat.BlueSlayerDirectionAnimationDrawStrat.class);

        kryo.register(TreasureGhostCollisionStrat.class);
        kryo.register(ArrowCollisionStrat.class);
        kryo.register(RotatingImageDrawStrat.ArrowImageDrawStrat.class);

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
        kryo.register(StopRequest.class);
        kryo.register(InitGameCommand.class);
        kryo.register(NewEntityCommand.class);
        kryo.register(RemoveEntityCommand.class);
        kryo.register(SetEntityCommand.class);
        kryo.register(SetTileCommand.class);
        kryo.register(ToClientCommand.class);
        kryo.register(UpdateResourceCommand.class);
        kryo.register(ActionRequest.class);
        kryo.register(GoDirectionRequest.class);

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
        kryo.register(ClientFinishMakingModelMsg.class);

        kryo.register(TeamWinCommand.class);
        kryo.register(NopDeathStrat.class);
        kryo.register(SlayerDeathStrat.class);

        kryo.register(WaterCollisionStrat.class);
        kryo.register(KingDeathStrat.class);
        kryo.register(ClientRestartMsg.class);
        kryo.register(AnimationDrawData.class);
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

    public static class ClientFinishMakingModelMsg {
        public ClientFinishMakingModelMsg() {}
    }
    public static class ClientRestartMsg {
        public ClientRestartMsg() {}
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

