package game.message.toServer;

import game.message.toClient.NewEntityCommand;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.Entity;

/**
 * Request sent by a client to the server that says the client
 * wants to upgrade an entity.
 */
public class UpgradeEntityRequest implements ToServerRequest {

    /**
     * Entity to be upgraded.
     */
    private long entityID;

    /**
     * Default constructor needed for serialization.
     */
    public UpgradeEntityRequest() {
    }

    /**
     * Constructor of the request, given an entity to be upgraded.
     *
     * @param entity entity to be made
     */
    public UpgradeEntityRequest(Entity entity) {
        this.entityID = entity.id;
    }

    /**
     * Updates the entity in the server.
     * @param model the game model on the game server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        Entity entity = model.getEntity(entityID);

        // Check if upgradable, not at max level, and if team has enough resources to upgrade.
        if (entity.has(Entity.EntityProperty.LEVEL) && entity.<Integer>get(Entity.EntityProperty.LEVEL) < 2 &&
                model.changeResource(entity.getTeam(),
                        TeamResourceData.levelToResource.get(entity.<Integer>get(Entity.EntityProperty.LEVEL) + 1), -10)) {
            entity.upgrade();
            model.processMessage(new NewEntityCommand(entity));
        }
    }
}
