package game.model.game.model.worldObject.entity.drawStrat;

import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntityData;
import images.Images;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static util.Util.toDrawCoords;

/*
simple draw strategy for drawing entire image on entity
 */
public abstract class ImageDrawStrat extends DrawStrat {

    public static final ImageDrawStrat TREE_IMAGE_DRAW_STRAT = make(Images.TREE_IMAGES[0]);
    public static final ImageDrawStrat STONE_IMAGE_DRAW_STRAT = make(Images.STONE_IMAGES[0]);
    public static final ImageDrawStrat METAL_IMAGE_DRAW_STRAT = make(Images.METAL_IMAGES[0]);
    public static final ImageDrawStrat WALL_IMAGE_DRAW_STRAT = make(Images.WALL_IMAGE);
    public static final ImageDrawStrat BOX_IMAGE_DRAW_STRAT = make(Images.BOX_IMAGE);

    @Override
    public DrawData initDrawData() {
        return null;
    }

    abstract Image getImage();

    public void draw(Entity entity, GraphicsContext gc) {
        gc.drawImage(getImage(), toDrawCoords(entity.data.x),
            toDrawCoords(entity.data.y),
            toDrawCoords(entity.data.hitbox.getWidth()),
            toDrawCoords(entity.data.hitbox.getHeight()));
    }

    public double getDrawZ(EntityData entity) {
        return entity.y;
    }

    private static ImageDrawStrat make(Image boxImage) {
        return new ImageDrawStrat() {
            @Override
            Image getImage() {
                return boxImage;
            }
        };
    }
}
