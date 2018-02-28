package game.model.game.model;

import com.esotericsoftware.minlog.Log;
import game.message.Message;
import game.message.toServer.MakeEntityRequest;
import game.message.toServer.RequestEntityRequest;
import game.message.toServer.RespawnSlayerRequest;
import game.model.game.grid.GridCell;
import game.model.game.map.ClientMapGenerator;
import game.model.game.map.Tile;
import game.model.game.model.gameState.GameState;
import game.model.game.model.gameState.Loading;
import game.model.game.model.gameState.Running;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.team.TeamRoleEntityMap;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Players;
import images.Images;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Cell;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import util.Util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.DRAW_STRAT;
import static util.Const.DEBUG_DRAW;
import static util.Util.toDrawCoords;
import static util.Util.toWorldCoords;

public class ClientGameModel extends GameModel {

    private TeamResourceData resourceData = new TeamResourceData();

    Team winningTeam = null;
    Team losingTeam = null;
    Team thisTeam;

    public ClientGameModel(Model server) {
        super(new ClientMapGenerator());
        this.server = server;
    }

    private long localPlayer;

    public Entity getLocalPlayer() {
        return this.getEntity(localPlayer);
    }

    public void setResourceData(TeamResourceData data) {
        resourceData = data;
    }

    public TeamResourceData getResourceData() {
        return resourceData;
    }

    public void setLocalPlayer(long localPlayer) {
        Log.info("Set local player");
        this.localPlayer = localPlayer;
        thisTeam = getEntity(localPlayer).getTeam();
    }

    public Team getTeam() {
        return thisTeam;
    }

    private Model server;

    @Override
    public void processMessage(Message m) {
        if (server == null)
            throw new RuntimeException("Cannot receive message before init()");
        if (m.sendToClient())
            this.queueMessage(m);
        if (m.sendToServer())
            server.processMessage(m);
    }

    public void init(Team team, Role role, TeamRoleEntityMap map, Tile[][] gameMap) {
        this.setLocalPlayer(map.getEntity(team, role));
        for (int x = 0; x < getMapWidth(); x++)
            for (int y = 0; y < getMapHeight(); y++)
                this.getCell(x, y).setTile(gameMap[x][y], this);
        state = Running.SINGLETON;
    }

    @Override
    public long nanoTime() {
        return server.nanoTime();
    }

    @Override
    public String toString() {
        return "Client game model";
    }

    public void requestEntityFromServer(long id) {
        server.processMessage(new RequestEntityRequest(id));
    }

    @Override
    public void execute(Consumer<ServerGameModel> serverAction, Consumer<ClientGameModel> clientAction) {
        clientAction.accept(this);
    }


    /**
     * returns approximately all the entities inside of the box centered at x,y with width, height
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    public void drawForeground(GraphicsContext gc, double x, double y, double w, double h) {

        List<GridCell> visable = new ArrayList<>();
        List<GridCell> explored = new ArrayList<>();
        List<GridCell> invis = new ArrayList<>();

        Team team = getTeam();
        for (int i = (int) Math.max(0,x); i < Math.min(Math.ceil(x + w), this.getMapWidth()-1); i++) {
            for (int j = (int) Math.max(0, y); j < Math.min(Math.ceil(y + h), this.getMapHeight()); j++) {
                GridCell cell = this.getCell(i, j);
                if (cell.isVisable(team)) {
                    visable.add(cell);
                } else if (cell.isExplored(team)) {
                    explored.add(cell);
                } else {
                    invis.add(cell);
                }
            }
        }

        visable.stream().flatMap(GridCell::streamContents).filter(entity -> entity.has(DRAW_STRAT)).sorted(Comparator.comparingDouble(Entity::getDrawZ)).forEach(a -> a.draw(gc, this));

        explored.forEach(cell -> {
            gc.drawImage(Images.FOG_GREY_EXPLORED_IMAGE, toDrawCoords(cell.getTopLeftX()-0.5), toDrawCoords(cell.getTopLeftY()-0.5), toDrawCoords(2.0), toDrawCoords(2.0));
        });

        invis.forEach(cell -> {
            gc.drawImage(Images.FOG_GREY_IMAGE, toDrawCoords(cell.getTopLeftX()-0.5), toDrawCoords(cell.getTopLeftY()-0.5), toDrawCoords(2), toDrawCoords(2));
      });

        if (DEBUG_DRAW)
            this.streamCells().flatMap(GridCell::streamContents).forEach(a -> a.getHitbox().draw(gc, a));
    }

    public void writeBackground(WritableImage image, boolean b) {
        this.streamCells().forEach(cell -> cell.draw(image.getPixelWriter(), this, b));
    }

    public void changeWinningTeam(Team team) {
        if (winningTeam == getTeam()) return; //already won
        winningTeam = team;
    }

    public void changeLosingTeam(Team team) {
        if (losingTeam == getTeam()) return; //already lost
        losingTeam = team;
    }

    public Team getWinningTeam() {
        return winningTeam;
    }

    public Team getLosingTeam() {
        return losingTeam;
    }

    private GameState state = Loading.SINGLETON;

    public GameState getState() {
        return state;
    }

    public void respawnSlayerRequest() {
        server.processMessage(new RespawnSlayerRequest(getTeam()));
    }
}
