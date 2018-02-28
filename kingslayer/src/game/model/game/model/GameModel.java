package game.model.game.model;

import game.message.Message;
import game.message.toClient.SetEntityCommand;
import game.message.toClient.SyncEntityFieldCommand;
import game.model.game.grid.GridCell;
import game.model.game.map.MapGenerator;
import game.model.game.map.Tile;
import game.model.game.model.worldObject.entity.Entity;
import util.Util;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static util.Const.*;

public abstract class GameModel implements Model {

    /**
     * grid of the game map. Each tile on the map is represented by a 1x1 cell.
     */
    private GridCell[][] grid;// = new GridCell[util.Const.GRID_X_SIZE][util.Const.GRID_Y_SIZE];

    private Collection<GridCell> allCells;

    private LinkedBlockingQueue<Message> messageQueue;

    private final Map<Long, Entity> entities;

    public boolean clientLoseControl = false;

    protected void queueMessage(Message message) {
        messageQueue.add(message);
    }

    public abstract void execute(Consumer<ServerGameModel> serverAction, Consumer<ClientGameModel> clientAction);

    //temporary
    //need to clean up
    public int respawnCnt = 0;
    public double respawnX;
    public double respawnY;

    /**
     * Constructor for the game model.
     * @param generator map generator for this game model
     */
    public GameModel(MapGenerator generator) {
        super();

        messageQueue = new LinkedBlockingQueue<>();
        grid = new GridCell[util.Const.GRID_X_SIZE][util.Const.GRID_Y_SIZE];

        entities = new HashMap<>();
        allCells = new ArrayList<>();

        for (int i = 0; i < util.Const.GRID_X_SIZE; i++)
            for (int j = 0; j < util.Const.GRID_Y_SIZE; j++)
                grid[i][j] = new GridCell(i, j);

        for (int i = 0; i < util.Const.GRID_X_SIZE; i++)
            for (int j = 0; j < util.Const.GRID_Y_SIZE; j++)
                grid[i][j].setTile(generator.makeTile(i, j), this);


        for (int i = 0; i < util.Const.GRID_X_SIZE; i++)
            for (int j = 0; j < util.Const.GRID_Y_SIZE; j++)
                allCells.add(grid[i][j]);

        generator.makeStartingEntities().forEach(e -> entities.put(e.id, e));
    }

    /**
     * Gets the map width in terms of number of grid cells.
     * @return the number of grid cells in the width of the map
     */
    public int getMapWidth() {
        return util.Const.GRID_X_SIZE;
    }

    /**
     * Gets the map height in terms of number of grid cells.
     * @return the number of grid cells in the height of the map
     */
    public int getMapHeight() {
        return util.Const.GRID_Y_SIZE;
    }

    /**
     * Gets the cell at the specified coordinates. The coordinates represent
     * the upper left corner of the cell.
     * @param x x-coordinate
     * @param y y-coordinate
     * @return the cell with the given upper left coordinates
     */
    public GridCell getCell(int x, int y) {
        return grid[x][y];
    }

    /**
     * Gets the tile at the specified coordinates. The coordinates represent
     * the upper left corner of the cell.
     * @param x x-coordinate
     * @param y y-coordinate
     * @return tile of the cell with the given upper left coordinates
     */
    public Tile getTile(int x, int y) {
        if (x >= getMapWidth() || x < 0 || y >= getMapHeight() || y < 0) {
            return Tile.DEEP_WATER;
        }
        return grid[x][y].getTile();
    }

    public Collection<Entity> getEntitiesAt(int x, int y) {
        return getCell(x, y).getContents();
    }

    /**
     * Removes the entity with the given ID from every tile on the game map.
     *
     * @param entityID ID of the entity to be removed
     */
    public void removeByID(long entityID) {
        if(entities.containsKey(entityID))
            remove(entities.get(entityID));
    }

    public void remove(Entity entity) {
        if(entity.containedIn != null)
            entity.containedIn.forEach(cell -> cell.removeContents(this, entity));
        entities.remove(entity.id);
    }

    public void update() {
        ArrayList<Message> list = new ArrayList<>();
        messageQueue.drainTo(list);
        list.forEach(m -> m.execute(this));

        long modelCurrentTime = this.nanoTime();

        entities.values().forEach(e -> e.update(this, modelCurrentTime));
        entities.values().forEach(e -> e.updateCells(this));
        allCells.forEach(cell -> cell.collideContents(this));
        allCells.forEach(cell -> cell.updatePeriodicLOS(this));

        this.execute(serverGameModel -> {
            entities.values().forEach(e -> {
                if(e.needSync) {
                    e.needSync = false;
                    serverGameModel.processMessage(new SetEntityCommand(e));
                }
                e.syncRequiredFeilds.forEach(syncFeild -> {
                    serverGameModel.processMessage(new SyncEntityFieldCommand(e, syncFeild));
                });
                e.syncRequiredFeilds.clear();
            });

        }, clientGameModel -> {});
    }

    public Collection<GridCell> getAllCells() {
        return allCells;
    }

    public Set<GridCell> getNeighbors(GridCell cell) {
        Set<GridCell> neighbors = new HashSet<>();
        for (int i = cell.getTopLeftX() - 1; i <= cell.getTopLeftX() + 1; i++) {
            for (int j = cell.getTopLeftY() - 1; j <= cell.getTopLeftY() + 1; j++) {
                if (Util.checkBounds(i, j)) {
                    neighbors.add(getCell(i, j));
                }
            }
        }
        return neighbors;
    }

    /*
    returns true on success
    returns false if unknown entity
     */
    public boolean trySetEntityData(long id, EnumMap<Entity.EntityProperty, Object> data) {
        if(entities.containsKey(id)) {
            entities.get(id).setData(data);
            return true;
        } else {
            return false;
        }
    }

    public void setEntity(Entity entity) {
        Entity e = entities.get(entity.id);
        if(e != null)
            e.setData(entity.getData());
        else
            entities.put(entity.id, entity);
    }


    public Collection<Entity> getAllEntities() {
        return entities.values();
    }

    public Entity getEntity(long entity) {
        if (!entities.containsKey(entity))
            return null;
        return entities.get(entity);
    }

    private long AINanoTime = -1;

    /*
    Server only mthod for updating the
     */
    public void updateAI(ServerGameModel serverGameModel) {
        if (AINanoTime == -1) {
            AINanoTime = System.nanoTime();
            return;
        }
        long cur = System.nanoTime();
        double elapsed = NANOS_TO_SECONDS * (cur - AINanoTime);
        AINanoTime = cur;
        entities.values().forEach(e -> e.updateAI(serverGameModel, elapsed));;
    }

    public Stream<GridCell> streamCells() {
        return getAllCells().stream();
    }

    public void slayerDead() {
        clientLoseControl = true;
    }
}
