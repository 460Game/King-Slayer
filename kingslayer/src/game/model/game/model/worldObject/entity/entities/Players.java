package game.model.game.model.worldObject.entity.entities;

import game.model.game.model.worldObject.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.aiStrat.AIDoNothingStrat;
import game.model.game.model.worldObject.entity.collideStrat.HardCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.SoftCollisionStrat;
import game.model.game.model.worldObject.entity.drawStrat.ImageDrawStrat;
import game.model.game.model.worldObject.entity.updateStrat.StillStrat;
import images.Images;

public class Players {



    public static Entity makeSlayer(Double x, Double y) {
        return new Entity(x, y,
            Team.ONE,
            StillStrat.make(),
            HardCollisionStrat.make(),
            ImageDrawStrat.make(Images.BOX_IMAGE),
            AIDoNothingStrat.make());
    }

    public static Entity makeKing(Double x, Double y) {
        return new Entity(x, y,
            Team.ONE,
            StillStrat.make(),
            SoftCollisionStrat.make(),
            ImageDrawStrat.make(Images.BOX_IMAGE),
            AIDoNothingStrat.make());
    }
}
