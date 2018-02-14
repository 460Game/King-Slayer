package game.model.game.model.worldObject.entity.drawStrat;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntityData;
import images.Images;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import static util.Util.toDrawCoords;

/*
simple drawFG strategy for drawing entire image on entity
 */
public abstract class ImageDrawStrat extends DrawStrat {

    public static final ImageDrawStrat TREE_IMAGE_DRAW_STRAT = new TreeImageDrawStrat();
    public static final ImageDrawStrat STONE_IMAGE_DRAW_STRAT = new StoneImageDrawStrat();
    public static final ImageDrawStrat METAL_IMAGE_DRAW_STRAT = new MetalImageDrawStrat();
    public static final ImageDrawStrat WALL_IMAGE_DRAW_STRAT = new WallImageDrawStrat();
    public static final ImageDrawStrat BOX_IMAGE_DRAW_STRAT = new BoxImageDrawStrat();
    public static final ImageDrawStrat TREASURE_IMAGE_DRAW_STRAT = new TreasureImageDrawStrat();

    public static final ImageDrawStrat WALL_BUILDABLE_IMAGE_DRAW_STRAT = new WallBuildableImageDrawStrat();

    @Override
    public DrawData initDrawData() {
        return null;
    }

    abstract Image getImage();

    abstract double getWidth();
    abstract double getHeight();
    abstract double getCenterX();
    abstract double getCenterY();


    public void draw(Entity entity, GraphicsContext gc) {
        double w = this.getWidth();
        double h = this.getHeight();
        double x = w - this.getCenterX();
        double y = h - this.getCenterY();

        gc.drawImage(getImage(),
            toDrawCoords(entity.data.x - x),
            toDrawCoords(entity.data.y - y),
            toDrawCoords(w),
            toDrawCoords(h));
    }

    public double getDrawZ(EntityData entity) {
        return entity.y;
    }

    public ImageDrawStrat() {
    }

    public static class TreasureImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage() {
            return Images.TREASURE_IMAGE;
        }

        @Override
        double getWidth() {
            return 0.6;
        }

        @Override
        double getHeight() {
            return 0.6;
        }

        @Override
        double getCenterX() {
            return 0.3;
        }

        @Override
        double getCenterY() {
            return 0.3;
        }

        private TreasureImageDrawStrat() {

        }
    }

    public static class TreeImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage() {
            return Images.TREE_IMAGES[0];
        }

        @Override
        double getWidth() {
            return 1;
        }

        @Override
        double getHeight() {
            return 1.5;
        }

        @Override
        double getCenterX() {
            return 0.5;
        }

        @Override
        double getCenterY() {
            return 0.5;
        }

        private TreeImageDrawStrat() {
        }
    }

    public static class StoneImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage() {
            return Images.STONE_IMAGES[0];
        }

        @Override
        double getWidth() {
            return 1;
        }

        @Override
        double getHeight() {
            return 1.5;
        }

        @Override
        double getCenterX() {
            return 0.5;
        }

        @Override
        double getCenterY() {
            return 0.5;
        }

        private StoneImageDrawStrat() {
        }
    }

    public static class MetalImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage() {
            return Images.METAL_IMAGES[0];
        }

        @Override
        double getWidth() {
            return 1;
        }

        @Override
        double getHeight() {
            return 1;
        }

        @Override
        double getCenterX() {
            return 0.5;
        }

        @Override
        double getCenterY() {
            return 0.5;
        }

        private MetalImageDrawStrat() {
        }
    }

    public static class WallImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage() {
            return Images.WALL_IMAGE;
        }

        @Override
        double getWidth() {
            return 1;
        }

        @Override
        double getHeight() {
            return 1.75;
        }

        @Override
        double getCenterX() {
            return 0.5;
        }

        @Override
        double getCenterY() {
            return 0.5;
        }

        private WallImageDrawStrat() {
        }
    }

    public static class BoxImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage() {
            return Images.BOX_IMAGE;
        }

        @Override
        double getWidth() {
            return 1;
        }

        @Override
        double getHeight() {
            return 1.5;
        }

        @Override
        double getCenterX() {
            return 0.5;
        }

        @Override
        double getCenterY() {
            return 0.5;
        }

        private BoxImageDrawStrat() {
        }
    }

    public static class WallBuildableImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage() {
            return Images.WALL_BUILDABLE_IMAGE;
        }

        @Override
        double getWidth() {
            return 1;
        }

        @Override
        double getHeight() {
            return 1.75;
        }

        @Override
        double getCenterX() {
            return 0.5;
        }

        @Override
        double getCenterY() {
            return 0.5;
        }

        private WallBuildableImageDrawStrat() {
        }
    }

}
