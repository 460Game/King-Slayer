package game.message.toServer;

import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.Entity;

/**
 * Message sent to create an entity in a client's game model. This message
 * is sent by the server.
 */
public class MakeEntityRequest implements ToServerRequest {

    /**
     * Entity to be made.
     */
    private Entity entity;

    private Team creator;

    private TeamResourceData.Resource resource;

    private int change;

    /**
     * Constructor of a message, given an entity to be created.
     * @param entity entity to be created
     */
    public MakeEntityRequest(Entity entity, Team creator, TeamResourceData.Resource resource, int change) {
        this.entity = entity;
        this.creator = creator;
        this.resource = resource;
        this.change = change;
    }

    /**
     * Default constructor needed for serialization.
     */
    public MakeEntityRequest(){}

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