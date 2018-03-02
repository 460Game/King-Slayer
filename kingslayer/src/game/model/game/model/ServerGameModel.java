package game.model.game.model;

import com.esotericsoftware.minlog.Log;
import game.ai.Astar;
import game.message.*;
import game.message.toClient.*;
import game.model.game.grid.GridCell;
import game.model.game.map.ServerMapGenerator;
import game.model.game.map.Tile;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.team.TeamRoleEntityMap;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.aiStrat.BuildingSpawnerStrat;
import game.model.game.model.worldObject.entity.collideStrat.CollisionStrat;
import lobby.PlayerInfo;
import util.Const;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.*;
import static util.Const.*;

public class ServerGameModel extends GameModel {

    public ServerGameModel() {
        super(new ServerMapGenerator(GRID_X_SIZE, GRID_Y_SIZE));
    }

    private Collection<? extends Model> clients = null;

    private Map<? extends Model, PlayerInfo> clientToPlayerInfo;

    private Astar astar;

    private Collection<GridCell> wood;

    private Collection<GridCell> stone;

    private Collection<GridCell> metal;

    private Collection<GridCell> team1collector;

    private Collection<GridCell> team2collector;

    private boolean addedBuilding;

    private Entity building;

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
        if (clients == null) {
            Log.info("Cannot receive message before init or after game end");
            return;
        }
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

        wood = new HashSet<>();
        stone = new HashSet<>();
        metal = new HashSet<>();
        team1collector = new HashSet<>();
        team2collector = new HashSet<>();
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
            Timer t2 = new Timer();
            t2.scheduleAtFixedRate(updateFPS, 1000, 1000);
        }

        updateTimerTask = new TimerTask() {
            public void run() {
                synchronized (updateTimerTask) {
                    doAICount[0]++;
                    totalFrameCount[0]++;

                    ServerGameModel.this.update();
                    if (doAICount[0] % Const.AI_LOOP_UPDATE_PER_FRAMES == 0) {
                        updateAI(ServerGameModel.this);
                        doAICount[0] = 0;
                    }

                    for (Model model : clients)
                        model.processMessage(new UpdateResourceCommand(teamData.get(clientToPlayerInfo.get(model).getTeam()))); //TEMPORARY GARBAGE
                }
            }
        };

        t.scheduleAtFixedRate(updateTimerTask, 10, 1000/60);
    }

    private Timer t = new Timer();

    public void stop() {
        updateTimerTask.cancel();
        t.cancel();

        synchronized (updateTimerTask){

        }




        running = false;
        clientToPlayerInfo = null;
        teamData = null;
        teamRoleEntityMap = null;
        clients = null;


        System.out.println("old server model stop");
    }

    public void teamWin(Team winTeam) {
        this.processMessage(new TeamWinCommand(winTeam));
//        stop();
    }

    public void makeEntity(Entity e) {
        this.setEntity(e);
        if (e.getCollideType() == CollisionStrat.CollideType.HARD)
            astar.updateModel(this);
        if (e.has(BUILDINGTYPE)) {
            addedBuilding = true;
            building = e;
        }
    }

    @Override
    public void execute(Consumer<ServerGameModel> serverAction, Consumer<ClientGameModel> clientAction) {
        serverAction.accept(this);
    }

    @Override
    public void removeByID(long entityID) {
        // Check if wall/tree/building/hard object is being removed.
        boolean isHard = false;
        if (getEntity(entityID).getCollideType() == CollisionStrat.CollideType.HARD)
            isHard = true;

        // Check if the entity removed is a tree. If so, remove that cell from the corresponding resource set.
        if (getEntity(entityID).has(RESOURCETYPE)) {
            if (getEntity(entityID).get(RESOURCETYPE) == TeamResourceData.Resource.WOOD)
                wood.remove(getCell((int) (double) getEntity(entityID).getX(), (int) (double) getEntity(entityID).getY()));
            else if (getEntity(entityID).get(RESOURCETYPE) == TeamResourceData.Resource.STONE)
                stone.remove(getCell((int) (double) getEntity(entityID).getX(), (int) (double) getEntity(entityID).getY()));
            else if (getEntity(entityID).get(RESOURCETYPE) == TeamResourceData.Resource.METAL)
                metal.remove(getCell((int) (double) getEntity(entityID).getX(), (int) (double) getEntity(entityID).getY()));
        } else if (getEntity(entityID).has(BUILDINGTYPE)) {
            if (getEntity(entityID).get(BUILDINGTYPE) == BuildingSpawnerStrat.BuildingType.COLLECTOR) {
                if (getEntity(entityID).getTeam() == Team.ONE)
                    team1collector.removeAll(getEntity(entityID).containedIn);
                else
                    team2collector.removeAll(getEntity(entityID).containedIn); // TODO support multiple teams?
            }
        }

        super.removeByID(entityID);
        clients.forEach(client -> client.processMessage(new RemoveEntityCommand(entityID)));

        // Update model used in astar if a hard object is removed.
        if (isHard)
            astar.updateModel(this);
    }

    public Astar getAstar() {
        return astar;
    }

    public Collection<GridCell> getWood() { return wood; }

    public Collection<GridCell> getStone() { return stone; }

    public Collection<GridCell> getMetal() { return metal; }

    public Collection<GridCell> getTeam1collector() { return team1collector; }

    public Collection<GridCell> getTeam2collector() { return team2collector; }

    @Override
    public void update() {
        super.update();
        if (wood.isEmpty()) {
            Set<Entity> woods = getAllEntities().stream().filter(e -> e.has(RESOURCETYPE)
                    && e.get(RESOURCETYPE) == TeamResourceData.Resource.WOOD).collect(Collectors.toSet());
            if (woods != null)
                for (Entity e : woods)
                    wood.addAll(e.containedIn);
        }
        if (stone.isEmpty()) {
            Set<Entity> stones = getAllEntities().stream().filter(e -> e.has(RESOURCETYPE)
                    && e.get(RESOURCETYPE) == TeamResourceData.Resource.STONE).collect(Collectors.toSet());
            if (stones != null)
                for (Entity e : stones)
                    stone.addAll(e.containedIn);
        }
        if (metal.isEmpty()) {
            Set<Entity> metals = getAllEntities().stream().filter(e -> e.has(RESOURCETYPE)
                    && e.get(RESOURCETYPE) == TeamResourceData.Resource.METAL).collect(Collectors.toSet());
            if (metals != null)
                for (Entity e : metals)
                    metal.addAll(e.containedIn);
        }
        if (addedBuilding) {
            addedBuilding = false;
            if (building.get(BUILDINGTYPE) == BuildingSpawnerStrat.BuildingType.COLLECTOR)
                if (building.getTeam() == Team.ONE)
                    team1collector.addAll(building.containedIn);
                else
                    team2collector.addAll(building.containedIn); // TODO support multiple teams?
        }
    }
}
