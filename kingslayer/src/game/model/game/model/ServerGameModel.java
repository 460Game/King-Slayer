package game.model.game.model;

import com.esotericsoftware.minlog.Log;
import game.message.*;
import game.message.toClient.*;
import game.model.game.map.ServerMapGenerator;
import game.model.game.map.Tile;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.team.TeamRoleEntityMap;
import game.model.game.model.worldObject.entity.Entity;
import lobby.PlayerInfo;
import util.Const;

import java.util.*;
import java.util.function.Consumer;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.TEAM;
import static util.Const.*;

public class ServerGameModel extends GameModel {

    public static boolean FPSPrint = true;

    public ServerGameModel() {
        super(new ServerMapGenerator(GRID_X_SIZE, GRID_Y_SIZE));
    }

    private Collection<? extends Model> clients = null;

    private Map<? extends Model, PlayerInfo> clientToPlayerInfo;

    private Thread updateThread;

    private Map<Team, TeamResourceData> teamData = new HashMap<>();

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
        if(clients == null)
            throw new RuntimeException("Cannot receive message before init()");
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
            if(entity.has(TEAM)) { //TODO this is TEMPORARY
                players.add(entity);
                teamRoleEntityMap.setEntity(entity.getTeam(), entity.getRole(), entity.id); // Only for players
            }
        }

        // Send all entities to clients
        for(Entity entity : this.getAllEntities())
            clients.forEach(client -> client.processMessage(new SetEntityCommand(entity)));

        Tile[][] tiles = new Tile[getMapWidth()][getMapHeight()];
        for(int x = 0; x <getMapWidth(); x++) {
            for(int y = 0; y < getMapHeight(); y++) {
                tiles[x][y] = this.getTile(x,y);
            }
        }

        // TODO @tian set each client to the role/team the want
        clients.forEach(client -> {
            Log.info("!!!!!!!client player: " + clientToPlayerInfo.get(client).getTeam() + clientToPlayerInfo.get(client).getRole());
            client.processMessage(new InitGameCommand(clientToPlayerInfo.get(client).getTeam(),
                    clientToPlayerInfo.get(client).getRole(), teamRoleEntityMap, tiles));
        });

        teamData.put(Team.ONE, new TeamResourceData());
        teamData.put(Team.TWO, new TeamResourceData());

        int i = 0;
        // Send player to client
        for(Model model : clients) {
            model.processMessage(new UpdateResourceCommand(teamData.get(players.get(i).getTeam())));
            i++;
        }
    }

    @Override
    public String toString() {
        return "Server game model";
    }

    private boolean running = false;

    public void start() {
        Log.info("Starting Server model");
        if (running) throw new RuntimeException("Cannot start server model when already running");

//        running = true;
//        updateThread = new Thread(this::run, this.toString() + " Update Thread");
//        updateThread.start();

        runWithTimerTask();

    }

    public void runWithTimerTask() {
        final int[] totalFrameCount = {0};
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

        boolean[] doAi = {false};

        TimerTask updateTimerTask = new TimerTask() {
            public void run() {
                totalFrameCount[0]++;

                ServerGameModel.this.update();
                if (doAi[0]) {
                    updateAI(ServerGameModel.this);
                    doAi[0] = false;
                }

                for(Model model : clients)
                    model.processMessage(new UpdateResourceCommand(teamData.get(clientToPlayerInfo.get(model).getTeam()))); //TEMPORARY GARBAGE
            }
        };

        Timer t = new Timer();
        t.scheduleAtFixedRate(updateTimerTask, 0, 1000/60);

    }

    public void stop() {
        running = false;
        updateThread.stop();
        clientToPlayerInfo = null;
        teamData = null;
        teamRoleEntityMap = null;
        clients = null;
        updateThread = null;
        System.out.println("old server model stop");
    }

    public void teamWin(Team winTeam) {
        for (Model clientModel : clients) {
            clientModel.processMessage(new TeamWinCommand(winTeam));
        }
        stop();
    }

    public boolean isRunning() {
        return running;
    }

    private void run() {
        boolean[] doAi = {false};

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                doAi[0] = true;
            }
        }, 1000, Const.AI_LOOP_UPDATE_TIME_MILI);


//        final int[] totalFrameCount = {0};
//        TimerTask updateFPS = new TimerTask() {
//            public void run() {
//                Log.info(String.valueOf("Server FPS: " + totalFrameCount[0]));
//                // display current totalFrameCount - previous,
//                // OR
//                // display current totalFrameCount, then set
//                totalFrameCount[0] = 0;
//            }
//        };
//
//        if (FPSPrint) {
//            Timer t = new Timer();
//            t.scheduleAtFixedRate(updateFPS, 1000, 1000);
//        }

//        TimerTask updateTimerTask = new TimerTask() {
//            public void run() {
//                totalFrameCount[0]++;
//
//                ServerGameModel.this.update();
//                if (doAi[0]) {
//                    updateAI(ServerGameModel.this);
//                    doAi[0] = false;
//                }
//                Thread.yield();
//                for(Model model : clients)
//                    model.processMessage(new UpdateResourceCommand(teamData.get(clientToPlayerInfo.get(model).getTeam()))); //TEMPORARY GARBAGE
//            }
//        };

        while (running) {

            this.update();
            if (doAi[0]) {
                this.updateAI(this);
                doAi[0] = false;
            }
            Thread.yield();
            for(Model model : clients)
                model.processMessage(new UpdateResourceCommand(teamData.get(clientToPlayerInfo.get(model).getTeam()))); //TEMPORARY GARBAGE
        }

        timer.cancel();
    }

    public void makeEntity(Entity e) {
        this.setEntity(e);
    }

    @Override
    public void execute(Consumer<ServerGameModel> serverAction, Consumer<ClientGameModel> clientAction) {
        serverAction.accept(this);
    }

    public void removeByID(long entityID) {
        super.removeByID(entityID);
        clients.forEach(client -> client.processMessage(new RemoveEntityCommand(entityID)));
    }

}
