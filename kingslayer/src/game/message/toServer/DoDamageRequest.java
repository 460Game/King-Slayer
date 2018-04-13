package game.message.toServer;

import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;


/**
 * Created by ryanhan on 4/8/18.
 */
public class DoDamageRequest implements ToServerRequest {

    double damage;

    /**
     * ID of the entity.
     */
    private long id;

    private Entity entity;

    /**
     * Default constructor needed for serialization.
     */
    public DoDamageRequest() {

    }

    /**
     * Constructor for a message, given an entity.
     * @param entity entity to be set
     */
    public DoDamageRequest(Entity entity, double damage) {
        this.damage = damage;
        this.id = entity.id;
        this.entity = entity;
    }

    /**
     * This guy is a tricky one!
     *
     * If entity with same UUID already exists in this model, should copy the new one into it.
     * If it doesn't exist copy the whole thing!
     */
    @Override
    public void executeServer(ServerGameModel model) {
        entity.decreaseHealthBy(model, damage);
    }
}
