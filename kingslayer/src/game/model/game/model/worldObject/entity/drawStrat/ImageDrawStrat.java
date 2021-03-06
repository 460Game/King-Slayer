package game.model.game.model.worldObject.entity.drawStrat;

import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Entity;
import images.Images;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

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

    abstract Image getImage(Entity entity);

    public abstract double getWidth();
    public abstract double getHeight();
    public abstract double getCenterX();
    public abstract double getCenterY();

@Override
    public void draw(Entity entity, ClientGameModel model, GraphicsContext gc) {
        double w = this.getWidth();
        double h = this.getHeight();
        double x = w - this.getCenterX();
        double y = h - this.getCenterY();

        gc.drawImage(getImage(entity),
            toDrawCoords(entity.getX() - x),
            toDrawCoords(entity.getY() - y),
            toDrawCoords(w),
            toDrawCoords(h));
    }

    public double getDrawZ(Entity entity) {
        return entity.getY();
    }

    public ImageDrawStrat() {
    }

    public static class TreasureImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage(Entity entity) {
            return Images.TREASURE_IMAGE;
        }

        @Override
        public double getWidth() {
            return 0.6;
        }

        @Override
        public double getHeight() {
            return 0.6;
        }

        @Override
        public double getCenterX() {
            return 0.3;
        }

        @Override
        public double getCenterY() {
            return 0.3;
        }

        private TreasureImageDrawStrat() {

        }
    }

    public static class TreeImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage(Entity entity) {
            int amount = entity.<Integer>get(Entity.EntityProperty.RESOURCE_AMOUNT);
            if (amount >= 15)
                return Images.TREE_IMAGES[0];
            else if (amount >= 12)
                return Images.TREE_IMAGES[1];
            else if (amount >= 9)
                return Images.TREE_IMAGES[2];
            else if (amount >= 6)
                return Images.TREE_IMAGES[3];
            else
                return Images.TREE_IMAGES[4];
        }

        @Override
        public double getWidth() {
            return 1;
        }

        @Override
        public double getHeight() {
            return 1.5;
        }

        @Override
        public double getCenterX() {
            return 0.5;
        }

        @Override
        public double getCenterY() {
            return 0.5;
        }

        private TreeImageDrawStrat() {
        }
    }

    public static class StoneImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage(Entity entity) {
            int amount = entity.<Integer>get(Entity.EntityProperty.RESOURCE_AMOUNT);
            if (amount >= 20)
                return Images.STONE_IMAGES[0];
            else if (amount >= 16)
                return Images.STONE_IMAGES[1];
            else if (amount >= 12)
                return Images.STONE_IMAGES[2];
            else if (amount >= 8)
                return Images.STONE_IMAGES[3];
            else
                return Images.STONE_IMAGES[4];
        }

        @Override
        public double getWidth() {
            return 1;
        }

        @Override
        public double getHeight() {
            return 1.5;
        }

        @Override
        public double getCenterX() {
            return 0.5;
        }

        @Override
        public double getCenterY() {
            return 0.5;
        }

        private StoneImageDrawStrat() {
        }
    }

    public static class MetalImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage(Entity entity) {
            int amount = entity.<Integer>get(Entity.EntityProperty.RESOURCE_AMOUNT);
            if (amount >= 30)
                return Images.METAL_IMAGES[0];
            else if (amount >= 25)
                return Images.METAL_IMAGES[2];
            else if (amount >= 20)
                return Images.METAL_IMAGES[4];
            else if (amount >= 15)
                return Images.METAL_IMAGES[6];
            else if (amount >= 10)
                return Images.METAL_IMAGES[8];
            else
                return Images.METAL_IMAGES[9];
        }

        @Override
        public double getWidth() {
            return 1;
        }

        @Override
        public double getHeight() {
            return 1.5;
        }

        @Override
        public double getCenterX() {
            return 0.5;
        }

        @Override
        public double getCenterY() {
            return 0.5;
        }

        private MetalImageDrawStrat() {
        }
    }

    public static class WallImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage(Entity entity) {
            return Images.WALL_IMAGE;
        }

        @Override
        public double getWidth() {
            return 1;
        }

        @Override
        public double getHeight() {
            return 1.75;
        }

        @Override
        public double getCenterX() {
            return 0.5;
        }

        @Override
        public double getCenterY() {
            return 0.5;
        }

        private WallImageDrawStrat() {
        }
    }

    public static class BoxImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage(Entity entity) {
            return Images.BOX_IMAGE;
        }

        @Override
        public double getWidth() {
            return 1;
        }

        @Override
        public double getHeight() {
            return 1.5;
        }

        @Override
        public double getCenterX() {
            return 0.5;
        }

        @Override
        public double getCenterY() {
            return 0.5;
        }

        private BoxImageDrawStrat() {
        }
    }

    public static class WallBuildableImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage(Entity entity) {
            return Images.WALL_BUILDABLE_IMAGE;
        }

        @Override
        public double getWidth() {
            return 1;
        }

        @Override
        public double getHeight() {
            return 1.75;
        }

        @Override
        public double getCenterX() {
            return 0.5;
        }

        @Override
        public double getCenterY() {
            return 0.5;
        }

        private WallBuildableImageDrawStrat() {
        }
    }

    public static class RedArrowTowerImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage(Entity entity) {
            return Images.RED_ARROW_TOWER_IMAGE;
        }

        @Override
        public double getWidth() {
            return 1;
        }

        @Override
        public double getHeight() {
            return 1.75;
        }

        @Override
        public double getCenterX() {
            return 0.5;
        }

        @Override
        public double getCenterY() {
            return 0.5;
        }

        private RedArrowTowerImageDrawStrat() {
        }
    }

    public static class BlueArrowTowerImageDrawStrat extends ImageDrawStrat {
        @Override
        Image getImage(Entity entity) {
            return Images.BLUE_ARROW_TOWER_IMAGE;
        }

        @Override
        public double getWidth() {
            return 1;
        }

        @Override
        public double getHeight() {
            return 1.75;
        }

        @Override
        public double getCenterX() {
            return 0.5;
        }

        @Override
        public double getCenterY() {
            return 0.5;
        }

        private BlueArrowTowerImageDrawStrat() {
        }
    }

}
