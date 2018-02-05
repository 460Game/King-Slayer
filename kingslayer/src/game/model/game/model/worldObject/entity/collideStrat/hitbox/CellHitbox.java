package game.model.game.model.worldObject.entity.collideStrat.hitbox;

/**
 * A hitbox that always takes up an entire cell, for entities that
 * block an entire cell.
 */
public class CellHitbox extends RectHitbox {

    public static final Hitbox SINGLETON = new CellHitbox();

    private CellHitbox() {
        super(1,1);
    }
}
