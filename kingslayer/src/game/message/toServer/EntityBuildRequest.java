package game.message.toServer;

import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntitySpawner;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.Hitbox;
import music.MusicPlayer;

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
     * Entity that created the new entity.
     */
    private Entity creator;

    private double x;
    private double y;

    private Hitbox hitbox;

    /**
     * Constructor of a message, given an entity to be created.
     * @param entitySpawner entity to be created
     */
    public EntityBuildRequest(EntitySpawner entitySpawner, Entity creator, double x, double y, Hitbox hitbox) {
        this.entity = entitySpawner;
        this.creator = creator;
        this.x = x;
        this.y = y;
        this.hitbox = hitbox;
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
        System.out.println(model.getCell((int) x, (int) y).isVisible(creator.getTeam()));
        if (//model.getCell((int) x, (int) y).isVisible(creator.getTeam()) &&
            !hitbox.getCollidesWith(model, x, y).findAny().isPresent() &&
            model.changeResource(creator.getTeam(), entity.resource, -entity.finalCost(model, creator.getTeam())))
            model.makeEntity(entity.makeEntity(x, y, creator.getTeam()));
      MusicPlayer.playConstructionSound();
    }
}
