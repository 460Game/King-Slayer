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

    public static ImageDrawStrat DRAW_EXMAPLE_STRAT = new ImageDrawStrat() {
        @Override
        Image getImage() {
            return Images.BOX_IMAGE;
        }
    };

    abstract Image getImage();

    public void draw(Entity entity, GraphicsContext gc) {
        gc.drawImage(getImage(), toDrawCoords(entity.data.shape.getX()),
            toDrawCoords(entity.data.shape.getY()),
            toDrawCoords(entity.data.shape.getWidth()),
            toDrawCoords(entity.data.shape.getHeight()));
    }

    public double getDrawZ(EntityData entity) {
        return entity.shape.getY();
    }
}
