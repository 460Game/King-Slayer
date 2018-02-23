package network;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.minlog.Log;
import de.javakaffee.kryoserializers.*;
import game.message.*;
import game.message.toClient.*;
import game.message.toServer.*;
import game.model.game.map.Tile;
import game.model.game.model.team.Role;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.team.TeamRoleEntityMap;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.aiStrat.BuildingSpawnerStrat;
import game.model.game.model.worldObject.entity.aiStrat.MinionStrat;
import game.model.game.model.worldObject.entity.collideStrat.*;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.*;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.deathStrat.KingDeathStrat;
import game.model.game.model.worldObject.entity.deathStrat.RemoveOnDeathStrat;
import game.model.game.model.worldObject.entity.deathStrat.SlayerDeathStrat;
import game.model.game.model.worldObject.entity.drawStrat.*;
import game.model.game.model.worldObject.entity.entities.NonMovingVelocity;
import game.model.game.model.worldObject.entity.entities.Velocity;
import game.model.game.model.worldObject.entity.updateStrat.*;

import java.lang.reflect.InvocationHandler;
import java.util.*;

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
        kryo.register(Velocity.class);
        kryo.register(InitGameCommand.class);
        kryo.register(UpdateResourceCommand.class);
        kryo.register(TeamResourceData.class);
        kryo.register(RequestEntityRequest.class);
        kryo.register(NewEntityCommand.class);
        kryo.register(Entity.class);


        kryo.register(ImageDrawStrat.WallImageDrawStrat.class);
        kryo.register(ImageDrawStrat.TreeImageDrawStrat.class);
        kryo.register(ImageDrawStrat.StoneImageDrawStrat.class);
        kryo.register(ImageDrawStrat.MetalImageDrawStrat.class);
        kryo.register(ImageDrawStrat.BoxImageDrawStrat.class);
        kryo.register(ImageDrawStrat.WallImageDrawStrat.class);
        kryo.register(ImageDrawStrat.WallBuildableImageDrawStrat.class);
        kryo.register(UpgradableImageDrawStrat.ResourceCollectorImageDrawStrat.class);
        kryo.register(UnitCollisionStrat.class);
        kryo.register(SoftCollisionStrat.class);
        kryo.register(HardCollisionStrat.class);
        kryo.register(GhostCollisionStrat.class);
        kryo.register(DirectionAnimationDrawStrat.SlayerDirectionAnimationDrawStrat.class);
        kryo.register(DirectionAnimationDrawStrat.KingDirectionAnimationDrawStrat.class);

        kryo.register(TreasureGhostCollisionStrat.class);
        kryo.register(ArrowCollisionStrat.class);
        kryo.register(RotatingImageDrawStrat.ArrowImageDrawStrat.class);

        kryo.register(DrawData.class);
        kryo.register(AnimateDrawStrat.class);
        kryo.register(DrawStrat.class);
        kryo.register(ImageDrawStrat.class);
        kryo.register(ShapeDrawStrat.class);
        kryo.register(MovingStrat.class);
        kryo.register(UpdateStrat.class);
        kryo.register(StopRequest.class);
        kryo.register(InitGameCommand.class);
        kryo.register(NewEntityCommand.class);
        kryo.register(RemoveEntityCommand.class);
        kryo.register(SetEntityCommand.class);
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
        kryo.register(SlayerDeathStrat.class);
        kryo.register(MinionStrat.class);
        kryo.register(BuildingSpawnerStrat.class);

        kryo.register(WaterCollisionStrat.class);
        kryo.register(KingDeathStrat.class);
        kryo.register(ClientRestartMsg.class);
        kryo.register(AnimationDrawData.class);

        kryo.register(RemoveOnDeathStrat.class);

        kryo.register( Arrays.asList( "" ).getClass(), new ArraysAsListSerializer() );
        kryo.register( Collections.EMPTY_LIST.getClass(), new CollectionsEmptyListSerializer() );
        kryo.register( Collections.EMPTY_MAP.getClass(), new CollectionsEmptyMapSerializer() );
        kryo.register( Collections.EMPTY_SET.getClass(), new CollectionsEmptySetSerializer() );
        kryo.register( Collections.singletonList( "" ).getClass(), new CollectionsSingletonListSerializer() );
        kryo.register( Collections.singleton( "" ).getClass(), new CollectionsSingletonSetSerializer() );
        kryo.register( Collections.singletonMap( "", "" ).getClass(), new CollectionsSingletonMapSerializer() );
        kryo.register( GregorianCalendar.class, new GregorianCalendarSerializer() );
        kryo.register( InvocationHandler.class, new JdkProxySerializer() );
        UnmodifiableCollectionsSerializer.registerSerializers( kryo );
        SynchronizedCollectionsSerializer.registerSerializers( kryo );

        kryo.register( EnumMap.class, new EnumMapSerializer() );
        kryo.register(EnumSet.class);
        kryo.register(NonMovingVelocity.class);
        kryo.register(SyncEntityFeildCommand.class);
        kryo.register(Entity.EntityProperty.class);
        kryo.register(Tile[].class);
        kryo.register(Tile[][].class);
//
        kryo.register(Entity.EntityProperty.class);
        kryo.register(game.model.game.map.Tile[].class);
        kryo.register(Tile[][].class);
        kryo.register(BuildingSpawnerStrat.SiegeBarracksBuildingSpawnerStrat.class);
        kryo.register(BuildingSpawnerStrat.MeleeBarracksBuildingSpawnerStrat.class);
        kryo.register(BuildingSpawnerStrat.ExplorationBarracksBuildingSpawnerStrat.class);
        kryo.register(BuildingSpawnerStrat.ResourceCollectorBuildingSpawnerStrat.class);
        kryo.register(BuildingSpawnerStrat.TowerBuildingSpawnerStrat.class);
        kryo.register(BuildingSpawnerStrat.BuildingSpawnerStratAIData.class);


        kryo.register(BuildingSpawnerStrat.SiegeBarracksBuildingSpawnerStrat.class);
        kryo.register(BuildingSpawnerStrat.TowerBuildingSpawnerStrat.class);
        kryo.register(BuildingSpawnerStrat.ExplorationBarracksBuildingSpawnerStrat.class);
        kryo.register(BuildingSpawnerStrat.MeleeBarracksBuildingSpawnerStrat.class);
        kryo.register(BuildingSpawnerStrat.RangedBarracksBuildingSpawnerStrat.class);
        kryo.register(BuildingSpawnerStrat.ResourceCollectorBuildingSpawnerStrat.class);



//        ImmutableListSerializer.registerSerializers( kryo );
//        ImmutableSetSerializer.registerSerializers( kryo );
//        ImmutableMapSerializer.registerSerializers( kryo );
//        ImmutableMultimapSerializer.registerSerializers( kryo );
//        ReverseListSerializer.registerSerializers( kryo );
//        UnmodifiableNavigableSetSerializer.registerSerializers( kryo );
//
//        ArrayListMultimapSerializer.registerSerializers( kryo );
//        HashMultimapSerializer.registerSerializers( kryo );
//        LinkedHashMultimapSerializer.registerSerializers( kryo );
//        LinkedListMultimapSerializer.registerSerializers( kryo );
//        TreeMultimapSerializer.registerSerializers( kryo );
    }

    public static class ClientMakeModelMsg {
        public ClientMakeModelMsg() {
        }
    }

    public static class ClientReadyMsg {
        private Team teamChoice;
        private Role roleChoice;
        private String playerName;
        public ClientReadyMsg() {}
        public ClientReadyMsg(Team team, Role role, String playerName) {
            teamChoice = team;
            roleChoice = role;
            this.playerName = playerName;
        }

        public Team getTeam() {
            return teamChoice;
        }
        public Role getRole() {
            return roleChoice;
        }
        public String getPlayerName() {
            return playerName;
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

