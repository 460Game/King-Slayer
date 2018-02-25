package game.model.game.model;

import com.esotericsoftware.minlog.Log;
import game.ai.Astar;
import game.message.*;
import game.message.toClient.*;
import game.model.game.map.ServerMapGenerator;
import game.model.game.map.Tile;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.team.TeamRoleEntityMap;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.collideStrat.CollisionStrat;
import lobby.PlayerInfo;
import util.Const;

import java.util.*;
import java.util.function.Consumer;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.PLAYER_NAME;
import static game.model.game.model.worldObject.entity.Entity.EntityProperty.TEAM;
import static util.Const.*;

public class ServerGameModel extends GameModel {

    public ServerGameModel() {
        super(new ServerMapGenerator(GRID_X_SIZE, GRID_Y_SIZE));
    }

    private Collection<? extends Model> clients = null;

    private Map<? extends Model, PlayerInfo> clientToPlayerInfo;

    private Astar astar;

    private Map<Team, TeamResourceData> teamData = new HashMap<>();
    private Thread updateThread;
    TimerTask updateTimerTask;

    public TeamRoleEntityMap teamRoleEntityMap = new TeamRoleEntityMap(NUM_TEAMS, NUM_ROLES);

    public Collection<? extends Model> getClients() {
        return clients;
    }

    public boolean changeResource(Team team, TeamResourceData.Resource r, int num) {
        if (teamData.get(team).getResource(r) + num >= 0 || num >= 0) {
            teamData.get(team).setResource(r, teamData.get(team).getResource(r) + num);
            return true;
        }
        return false;
    }

    @Override
    public void processMessage(Message m) {
        if (clients == null)
            throw new RuntimeException("Cannot receive message before init or after game end");
        if (m.sendToServer())
            this.queueMessage(m);
        if (m.sendToClient())
            clients.forEach(model -> model.processMessage(m));
    }

    @Override
    public long nanoTime() {
        return System.nanoTime();
    }

    public void init(Collection<? extends Model> clients, Map<? extends Model, PlayerInfo> clientToPlayerInfoMap) {

        this.clients = clients;
        this.clientToPlayerInfo = clientToPlayerInfoMap;

        ArrayList<Entity> players = new ArrayList<>();
        for (Entity entity : this.getAllEntities()) {
            if (entity.has(TEAM)) { //TODO this is TEMPORARY
                players.add(entity);
                teamRoleEntityMap.setEntity(entity.getTeam(), entity.getRole(), entity.id); // Only for players
            }
        }

        for (Entity entity : players) {
            entity.setOrAdd(PLAYER_NAME, "");
        }

        clients.forEach(client -> {
            getEntity(teamRoleEntityMap.getEntity(clientToPlayerInfo.get(client).getTeam(),
                    clientToPlayerInfo.get(client).getRole())).setOrAdd(PLAYER_NAME,
                    clientToPlayerInfoMap.get(client).getPlayerName());
//            System.out.println((String)getEntity(teamRoleEntityMap.getEntity(clientToPlayerInfo.get(client).getTeam(),
//                    clientToPlayerInfo.get(client).getRole())).get(PLAYER_NAME));
        });

        // Send all entities to clients
        for (Entity entity : this.getAllEntities())
            this.processMessage(new NewEntityCommand(entity));

        Tile[][] tiles = new Tile[getMapWidth()][getMapHeight()];
        for (int x = 0; x < getMapWidth(); x++) {
            for (int y = 0; y < getMapHeight(); y++) {
                tiles[x][y] = this.getTile(x, y);
            }
        }

        teamData.put(Team.ONE, new TeamResourceData());
        teamData.put(Team.TWO, new TeamResourceData());

        int i = 0;
        // Send player to client
        for (Model model : clients) {
            model.processMessage(new UpdateResourceCommand(teamData.get(players.get(i).getTeam())));
            i++;
        }

        // TODO @tian set each client to the role/team the want
        clients.forEach(client -> {
            Log.info("!!!!!!!client player: " +
                    clientToPlayerInfo.get(client).getTeam() + clientToPlayerInfo.get(client).getRole());
            client.processMessage(new InitGameCommand(clientToPlayerInfo.get(client).getTeam(),
                    clientToPlayerInfo.get(client).getRole(), teamRoleEntityMap, tiles));
        });

        astar = new Astar(this);
    }

    @Override
    public String toString() {
        return "Server game model";
    }

    private boolean running = false;

    public void start() {
        Log.info("Starting Server model");
        if (running) throw new RuntimeException("Cannot start server model when already running");
        running = true;
//
//        final int[] totalFrameCount = {0};
//        final int[] doAICount = {0};
//
//        TimerTask updateFPS = new TimerTask() {
//            public void run() {
//                Log.info(String.valueOf("Server FPS: " + totalFrameCount[0]));
//                totalFrameCount[0] = 0;
//            }
//        };
//
//        if (FPSPrint) {
//            Timer t = new Timer();
//            t.scheduleAtFixedRate(updateFPS, 1000, 1000);
//        }
//
//        TimerTask updateTimerTask = new TimerTask() {
//            public void run() {
//                doAICount[0]++;
//                totalFrameCount[0]++;
//
//                ServerGameModel.this.update();
//                if (doAICount[0] % Const.AI_LOOP_UPDATE_PER_FRAMES == 0) {
//                    updateAI(ServerGameModel.this);
//                    doAICount[0] = 0;
//                }
//
//                for (Model model : clients)
//                    model.processMessage(new UpdateResourceCommand(teamData.get(clientToPlayerInfo.get(model).getTeam()))); //TEMPORARY GARBAGE
//            }
//        };
//
//        t.scheduleAtFixedRate(updateTimerTask, 0, 1000 / 60);
        running = true;
//        updateThread = new Thread(this::runWithTimerTask, this.toString() + " Update Thread");
//        updateThread.start();
        runWithTimerTask();
    }

    public void runWithTimerTask() {
        final int[] totalFrameCount = {0};
        final int[] doAICount = {0};

        TimerTask updateFPS = new TimerTask() {
            public void run() {
                Log.info(String.valueOf("Server FPS: " + totalFrameCount[0]));
                totalFrameCount[0] = 0;
            }
        };

        if (FPSPrint) {
            Timer t = new Timer();
            t.scheduleAtFixedRate(updateFPS, 1000, 1000);
        }

        updateTimerTask = new TimerTask() {
            public void run() {
                doAICount[0]++;
                totalFrameCount[0]++;

                ServerGameModel.this.update();
                if (doAICount[0]%Const.AI_LOOP_UPDATE_PER_FRAMES == 0) {
                    updateAI(ServerGameModel.this);
                    doAICount[0] = 0;
                }

                for(Model model : clients)
                    model.processMessage(new UpdateResourceCommand(teamData.get(clientToPlayerInfo.get(model).getTeam()))); //TEMPORARY GARBAGE
            }
        };

        Timer t = new Timer();
        t.scheduleAtFixedRate(updateTimerTask, 10, 1000/60);
    }

    private Timer t = new Timer();

    public void stop() {
        running = false;
        clientToPlayerInfo = null;
        teamData = null;
        teamRoleEntityMap = null;
        clients = null;
        t.cancel();

        updateTimerTask.cancel();

        System.out.println("old server model stop");
    }

    public void teamWin(Team winTeam) {
        this.processMessage(new TeamWinCommand(winTeam));
        stop();
    }

    public void makeEntity(Entity e) {
        this.setEntity(e);
        if (e.getCollideType() == CollisionStrat.CollideType.HARD)
            astar.updateModel(this);
    }

    @Override
    public void execute(Consumer<ServerGameModel> serverAction, Consumer<ClientGameModel> clientAction) {
        serverAction.accept(this);
    }

    public void removeByID(long entityID) {
        boolean isHard = false;
        if (getEntity(entityID).getCollideType() == CollisionStrat.CollideType.HARD)
            isHard = true;
        super.removeByID(entityID);
        clients.forEach(client -> client.processMessage(new RemoveEntityCommand(entityID)));
        if (isHard)
            astar.updateModel(this);
    }

    public Astar getAstar() {
        return astar;
    }
}
