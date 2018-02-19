package game.message.toServer;

import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.Entity;

/**
 * Message sent to create an entity in a client's game model. This message
 * is sent by the server.
 */
public class EntityBuildRequest implements ToServerRequest {

    /**
     * Entity to be made.
     */
    private Entity entity;

    /**
     * Team that created the entity.
     */
    private Team creator;

    /**
     * Resource that may be changed.
     */
    private TeamResourceData.Resource resource;

    /**
     * Amount of resource that was changed.
     */
    private int change;

    /**
     * Constructor of a message, given an entity to be created.
     * @param entity entity to be created
     * @param creator team creating the entity
     * @param resource resource that may be changed
     * @param change amount of resource changed
     */
    public EntityBuildRequest(Entity entity, Team creator, TeamResourceData.Resource resource, int change) {
        this.entity = entity;
        this.creator = creator;
        this.resource = resource;
        this.change = change;
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
        if (model.changeResource(creator, resource, change))
            model.makeEntity(entity);
    }
}
