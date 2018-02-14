package game.model.game.model;

import com.esotericsoftware.minlog.Log;
import game.message.*;
import game.message.toClient.*;
import game.model.game.map.ServerMapGenerator;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.team.TeamRoleEntityMap;
import game.model.game.model.worldObject.entity.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static util.Const.*;

public class ServerGameModel extends GameModel {

    public ServerGameModel() {
        super(new ServerMapGenerator(GRID_X_SIZE, GRID_Y_SIZE));
    }

    private Collection<? extends Model> clients = null;

    private Map<Team, TeamResourceData> teamData = new HashMap<>();

    private TeamRoleEntityMap teamRoleEntityMap = new TeamRoleEntityMap(NUM_TEAMS, NUM_ROLES);

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

    public void init(Collection<? extends Model> clients) {

        this.clients = clients;

        // Send teamRoleEntityMap to client
        for(Model client : clients)
            for (int i = 0; i < this.getMapWidth(); i++)
                for (int j = 0; j < this.getMapWidth(); j++) {
                    client.processMessage(new SetTileMessage(i, j, this.getTile(i, j)));
                }

        ArrayList<Entity> players = new ArrayList<>();
        for (Entity entity : this.getAllEntities()) {
            if(entity.team != Team.NEUTRAL) { //TODO this is TEMPORARY
                players.add(entity);
                teamRoleEntityMap.setEntity(entity.team, entity.role, entity.id); // Only for players
            }
        }

        // Send all entities to clients
        for(Entity entity : this.getAllEntities())
            clients.forEach(client -> client.processMessage(new SetEntityMessage(entity)));

        // TODO @tian set each client to the role/team the want
        clients.forEach(client -> client.processMessage(new InitGameMessage(Team.ONE, Role.KING, teamRoleEntityMap)));

        teamData.put(Team.ONE, new TeamResourceData());
        teamData.put(Team.TWO, new TeamResourceData());

        int i = 0;
        // Send player to client
        for(Model model : clients) {
            model.processMessage(new UpdateResourcesMessage(teamData.get(players.get(i).team)));
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
        running = true;
        (new Thread(this::run, this.toString() + " Update Thread")).start();
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    private void run() {
        while (running) {
        //    long start = System.nanoTime();
            this.update();
            //want it independent of how long update take, so use the following instead
            //of thread.sleep()...
        //    long delta = System.nanoTime()- start;
        //    if (UPDATE_LOOP_TIME_NANOS > delta)
         //       try {
                    Thread.yield();
              //      Thread.sleep((UPDATE_LOOP_TIME_NANOS - delta)/ 1000000L);
             //   } catch (InterruptedException e) {
             //       e.printStackTrace();
             //   }

            teamData.get(Team.ONE).increaseWood(1);
            for(Model model : clients) {
                model.processMessage(new UpdateResourcesMessage(teamData.get(Team.ONE))); //TEMPORARY GARBAGE
            }

        }
    }

    public void makeEntity(Entity e) {
        this.setEntity(e);
        clients.forEach(client -> client.processMessage(new SetEntityMessage(e)));
    }
}
