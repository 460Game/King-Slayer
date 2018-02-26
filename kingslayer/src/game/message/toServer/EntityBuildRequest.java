package game.message.toServer;

import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntitySpawner;

/**
 * Message sent to create an entity in a client's game model. This message
 * is sent by the server.
 */
public class EntityBuildRequest implements ToServerRequest {

    /**
     * Entity to be made.
     */
    private EntitySpawner entity;

    /**
     * Team that created the entity.
     */
    private Team creator;

    private double x;
    private double y;

    /**
     * Constructor of a message, given an entity to be created.
     * @param entitySpawner entity to be created
     */
    public EntityBuildRequest(EntitySpawner entitySpawner, Team team, double x, double y) {
        this.entity = entitySpawner;
        this.creator = team;
        this.x = x;
        this.y = y;
    }

    /**
     * Default constructor needed for serialization.
     */
    public EntityBuildRequest(){}

    /**
     * Make the entity in the server model.
     * @param model the game model on the server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        if (model.changeResource(creator, entity.resource, entity.cost))
            model.makeEntity(entity.makeEntity(x, y, creator));
    }
}
