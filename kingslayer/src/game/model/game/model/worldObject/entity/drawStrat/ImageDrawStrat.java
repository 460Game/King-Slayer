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

    public static final ImageDrawStrat TREE_IMAGE_DRAW_STRAT = make(Images.TREE_IMAGES[0]);

    private static ImageDrawStrat make(Image boxImage) {
        return new ImageDrawStrat() {
            @Override
            Image getImage() {
                return boxImage;
            }
        };
    }
}
