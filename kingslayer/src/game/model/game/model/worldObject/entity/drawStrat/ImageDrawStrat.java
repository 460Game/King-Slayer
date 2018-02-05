package game.model.game.model.worldObject.entity.drawStrat;

import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntityData;
import game.model.game.model.worldObject.entity.updateStrat.UpdateStrat;
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
        gc.drawImage(getImage(), toDrawCoords(entity.data.x),
            toDrawCoords(entity.data.y),
            toDrawCoords(entity.data.collisionData.getWidth()),
            toDrawCoords(entity.data.collisionData.getHeight()));
    }

    public double getDrawZ(EntityData entity) {
        return entity.y;
    }

    public static DrawStrat make(Image boxImage) {
        return new ImageDrawStrat() {
            @Override
            public DrawData initDrawData() {
                return null;
            }

            @Override
            Image getImage() {
                return boxImage;
            }
        };
    }
}
