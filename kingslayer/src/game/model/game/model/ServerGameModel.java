package game.model.game.model;

import com.esotericsoftware.minlog.Log;
import game.ai.Astar;
import game.message.*;
import game.message.toClient.*;
import game.message.toServer.RemoveEntityRequest;
import game.model.game.grid.GridCell;
import game.model.game.map.ServerMapGenerator;
import game.model.game.map.Tile;
import game.model.game.model.team.Role;
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
import static util.Util.random;

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

    private double[][][] cellsToGoTo;

    private double[] totalProbability;

    private boolean addedBuilding;

    private Entity building;

    private final Object lock = new Object();

    private TimerTask updateFPS;
    private Timer t2;

    private Map<Team, TeamResourceData> teamData = new HashMap<>();
    private TimerTask updateTimerTask;

    private Map<Team, Long> kingMap = new HashMap<>();

    private Map<Team, List<Long>> slayerMap = new HashMap<>();


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

    public long getKing(Team team) {
        return kingMap.get(team);
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

        for(Team team : Team.values())
            slayerMap.put(team, new ArrayList<>());

        ArrayList<Entity> players = new ArrayList<>();
        for (Entity entity : this.getAllEntities()) {
            if (entity.has(TEAM)) {
                if(entity.has(ROLE)) {
                    if(entity.get(ROLE) == Role.KING) {
                        kingMap.put(entity.getTeam(), entity.id);
                    } else if(entity.get(ROLE) == Role.SLAYER) {
                        slayerMap.get(entity.getTeam()).add(entity.id);
                    }
                    players.add(entity);
                }
            }
        }

        for (Entity entity : players) {
            entity.setOrAdd(PLAYER_NAME, "");
        }

        // Send all entities to clients
        for (Entity entity : this.getAllEntities())
            this.processMessage(new NewEntityCommand(entity));

        Tile[][] tiles = new Tile[getMapWidth()][getMapHeight()];
        for (int x = 0; x < getMapWidth(); x++) {
            for (int y = 0; y < getMapHeight(); y++) {
                tiles[x][y] = this.getTile(x, y);
            }
        }

        cellsToGoTo = new double[NUM_TEAMS][getMapWidth()][getMapHeight()];
        totalProbability = new double[NUM_TEAMS];
        for (int x = 0; x < NUM_TEAMS; x++) {
            for (int y = 0; y < getMapWidth(); y++) {
                for (int z = 0; z < getMapHeight(); z++) {
                    if (y < 4 || z > 95)
                        cellsToGoTo[x][y][z] = 0;
                    else {
                        cellsToGoTo[x][y][z] = 1.0 / (getMapHeight() * getMapWidth());
                        totalProbability[x] += 1.0 / (getMapHeight() * getMapWidth());
                    }
                }
            }
        }

        teamData.put(Team.RED_TEAM, new TeamResourceData());
        teamData.put(Team.BLUE_TEAM, new TeamResourceData());

        clients.forEach(client -> {


            Role playerRole = clientToPlayerInfo.get(client).getRole();
            Team playerTeam = clientToPlayerInfo.get(client).getTeam();
            long playersid;
            if(playerRole == Role.KING)
                playersid = kingMap.get(playerTeam);
            else
                playersid = slayerMap.get(playerTeam).remove(0);

            getEntity(playersid).setOrAdd(PLAYER_NAME,
                    clientToPlayerInfoMap.get(client).getPlayerName());

            client.processMessage(new InitGameCommand(playerTeam,
                    playerRole, playersid , tiles));
        });

        for(List l : slayerMap.values()) {
            l.forEach(e -> this.processMessage(new RemoveEntityRequest(getEntity((long)e))));
        }

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

        updateFPS = new TimerTask() {
            public void run() {
                synchronized (lock) {
                    Log.info(String.valueOf("Server FPS: " + totalFrameCount[0]));
                    totalFrameCount[0] = 0;
                }
            }
        };

        if (FPSPrint) {
            t2 = new Timer();
            t2.scheduleAtFixedRate(updateFPS, 1000, 1000);
        }

        updateTimerTask = new TimerTask() {
            public void run() {
                synchronized (lock) {

                    doAICount[0]++;
                    totalFrameCount[0]++;

                    if (clients != null) {
                        if (doAICount[0] % Const.AI_LOOP_UPDATE_PER_FRAMES == 0) {
                            updateAI(ServerGameModel.this);
                            clients.forEach(model -> model.processMessage(new UpdateResourceCommand(teamData.get(clientToPlayerInfo.get(model).getTeam()))));
                            doAICount[0] = 0;
                        }
                    }

                    ServerGameModel.this.update();
                }
            }
        };

        t.scheduleAtFixedRate(updateTimerTask, 10, 1000 / UPDATES_PER_SECOND); //
    }

    private Timer t = new Timer();

    public void stop() {

        if (!running) {
            return;
        }

        synchronized (lock) {

            updateTimerTask.cancel();
            t.cancel();
            if (FPSPrint) {
                t2.cancel();
            }
            updateFPS.cancel();


            running = false;
            this.clientToPlayerInfo.clear();
            clientToPlayerInfo = null;
            teamData = null;
            clients.clear();
            clients = null;
            astar = null;
            stone.clear();
            stone = null;
            wood.clear();
            wood = null;
            team1collector.clear();
            team1collector = null;
            team2collector.clear();
            team2collector = null;
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        System.out.println("old server model stop");
    }

    public void teamWin(Team winTeam) {
        this.processMessage(new TeamWinCommand(winTeam));
//        clients = null;
        stop();
    }

    public void makeEntity(Entity e) {
        this.setEntity(e);
        if (e.getCollideType() == CollisionStrat.CollideType.HARD) {
            astar.updateModel(this);
            for (int i = 0; i < NUM_TEAMS; i++)
                setCellsToGoTo(i, (int) (double) e.getX(), (int) (double) e.getY(), 0);
        }
        if (e.has(BUILDING_TYPE)) {
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

        int locx = 0;
        int locy = 0;

        Entity entity = getEntity(entityID);

        // Check if wall/tree/building/hard object is being removed.
        boolean isHard = false;
        if (entity.getCollideType() == CollisionStrat.CollideType.HARD) {
            isHard = true;
            locx = (int) (double) entity.getX();
            locy = (int) (double) entity.getY();
        }

        // Check if the entity removed is a tree. If so, remove that cell from the corresponding resource set.
        if (entity.has(RESOURCE_TYPE)) {
            if (entity.get(RESOURCE_TYPE) == TeamResourceData.Resource.WOOD)
                wood.remove(getCell((int) (double) entity.getX(), (int) (double) entity.getY()));
            else if (entity.get(RESOURCE_TYPE) == TeamResourceData.Resource.STONE)
                stone.remove(getCell((int) (double) entity.getX(), (int) (double) entity.getY()));
            else if (entity.get(RESOURCE_TYPE) == TeamResourceData.Resource.METAL)
                metal.remove(getCell((int) (double) entity.getX(), (int) (double) entity.getY()));
        } else if (entity.has(BUILDING_TYPE)) {
            if (entity.get(BUILDING_TYPE) == BuildingSpawnerStrat.BuildingType.COLLECTOR) {
                if (entity.getTeam() == Team.RED_TEAM)
                    team1collector.removeAll(entity.containedIn);
                else
                    team2collector.removeAll(entity.containedIn); // TODO support multiple teams?
            }
        }

        super.removeByID(entityID);
        clients.forEach(client -> client.processMessage(new RemoveEntityCommand(entityID)));

        // Update model used in astar if a hard object is removed.
        if (isHard) {
            for (int i = 0; i < NUM_TEAMS; i++)
                setCellsToGoTo(i, locx, locy, 1.0 / (getMapWidth() * getMapHeight())); // TODO may need to change this value
            astar.updateModel(this);
        }
    }

    public Astar getAstar() {
        return astar;
    }

    public Collection<GridCell> getWood() {
        return wood;
    }

    public Collection<GridCell> getStone() {
        return stone;
    }

    public Collection<GridCell> getMetal() {
        return metal;
    }

    public Collection<GridCell> getTeam1collector() {
        return team1collector;
    }

    public Collection<GridCell> getTeam2collector() {
        return team2collector;
    }

    public TeamResourceData getTeamData(Team team) {
        return teamData.get(team);
    }

    public double[][][] getCellsToGoTo() {
        return cellsToGoTo;
    }

    public void setCellsToGoTo(int team, int x, int y, double probability) {
        boolean changed = false;
        if (cellsToGoTo[team][x][y] != probability)
            changed = true;

        totalProbability[team] -= cellsToGoTo[team][x][y];
        cellsToGoTo[team][x][y] = probability;
        totalProbability[team] += probability;

        if (changed)
            adjustCellsToGoTo(team);
    }

    public void adjustCellsToGoTo(int team) {
        for (int x = 0; x < getMapWidth(); x++) {
            for (int y = 0; y < getMapHeight(); y++) {
                cellsToGoTo[team][x][y] /= totalProbability[team];
            }
        }
        totalProbability[team] = 1;
    }

    public GridCell getNextCell(int team) {
        double value = random.nextDouble();
        double total = 0;
        GridCell returnCell = getCell(0, 0);

        for (int i = 0; i < getMapWidth(); i++) {
            for (int j = 0; j < getMapHeight(); j++) {
                total += cellsToGoTo[team][i][j];
                if (total >= value)
                    return getCell(i, j);
            }
        }

        return returnCell;
    }

    @Override
    public void update() {
        super.update();

      //  entities.values().parallelStream().forEach(e -> {
          entities.values().forEach(e -> {
            if(e.needSync) {
                e.needSync = false;
                this.processMessage(new SetEntityCommand(e));
            }
            e.syncRequiredFeilds.forEach(syncFeild -> {
                this.processMessage(new SyncEntityFieldCommand(e, syncFeild));
            });
            e.syncRequiredFeilds.clear();
        });

        if (wood.isEmpty()) {
            Set<Entity> woods = getAllEntities().stream().filter(e -> e.has(RESOURCE_TYPE)
                    && e.get(RESOURCE_TYPE) == TeamResourceData.Resource.WOOD).collect(Collectors.toSet());
            if (woods != null)
                for (Entity e : woods)
                    wood.addAll(e.containedIn);
        }
        if (stone.isEmpty()) {
            Set<Entity> stones = getAllEntities().stream().filter(e -> e.has(RESOURCE_TYPE)
                    && e.get(RESOURCE_TYPE) == TeamResourceData.Resource.STONE).collect(Collectors.toSet());
            if (stones != null)
                for (Entity e : stones)
                    stone.addAll(e.containedIn);
        }
        if (metal.isEmpty()) {
            Set<Entity> metals = getAllEntities().stream().filter(e -> e.has(RESOURCE_TYPE)
                    && e.get(RESOURCE_TYPE) == TeamResourceData.Resource.METAL).collect(Collectors.toSet());
            if (metals != null)
                for (Entity e : metals)
                    metal.addAll(e.containedIn);
        }
        if (addedBuilding) {
            addedBuilding = false;
            if (building.get(BUILDING_TYPE) == BuildingSpawnerStrat.BuildingType.COLLECTOR)
                if (building.getTeam() == Team.RED_TEAM)
                    team1collector.addAll(building.containedIn);
                else
                    team2collector.addAll(building.containedIn); // TODO support multiple teams?
        }
    }
}
