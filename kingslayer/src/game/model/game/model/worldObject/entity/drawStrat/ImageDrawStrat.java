package game.model.game.model.worldObject.entity.drawStrat;

import com.esotericsoftware.minlog.Log;
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

    public static final ImageDrawStrat TREE_IMAGE_DRAW_STRAT = new TreeImageDrawStrat();
    public static final ImageDrawStrat STONE_IMAGE_DRAW_STRAT = new StoneImageDrawStrat();
    public static final ImageDrawStrat METAL_IMAGE_DRAW_STRAT = new MetalImageDrawStrat();
    public static final ImageDrawStrat WALL_IMAGE_DRAW_STRAT = new WallImageDrawStrat();
    public static final ImageDrawStrat BOX_IMAGE_DRAW_STRAT = new BoxImageDrawStrat();

    private static class TreeImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage() {
            return Images.TREE_IMAGES[0];
        }

        private TreeImageDrawStrat() {
        }
    }

    private static class StoneImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage() {
            return Images.STONE_IMAGES[0];
        }

        private StoneImageDrawStrat() {
        }
    }

    private static class MetalImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage() {
            return Images.METAL_IMAGES[0];
        }

        private MetalImageDrawStrat() {
        }
    }

    private static class WallImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage() {
            return Images.WALL_IMAGE;
        }

        private WallImageDrawStrat() {
        }
    }

    private static class BoxImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage() {
            return Images.BOX_IMAGE;
        }

        private BoxImageDrawStrat() {
        }
    }

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

    private ImageDrawStrat() {
    }
}
