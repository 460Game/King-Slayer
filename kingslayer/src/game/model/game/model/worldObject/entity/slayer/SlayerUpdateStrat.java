package game.model.game.model.worldObject.entity.slayer;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Velocity;
import game.model.game.model.worldObject.entity.updateStrat.MovingStrat;

public class SlayerUpdateStrat extends MovingStrat {
    public static final SlayerUpdateStrat SINGLETON = new SlayerUpdateStrat();
    private SlayerUpdateStrat() {}

    @Override
    public void update(Entity entity, GameModel model) {
        SlayerData slayerData = SlayerData.copyOf((SlayerData) entity.get(Entity.EntityProperty.SLAYER_DATA));

        if (slayerData.meleeLastTime > 0) {

            slayerData.meleeLastTime -= entity.timeDelta;
            entity.set(Entity.EntityProperty.SLAYER_DATA, slayerData);
            Velocity v = new Velocity().withMagnitude(SlayerData.meleeSpeed).withAngle(slayerData.meleeAngle);
            entity.translateX( v.getVx()
                    * entity.timeDelta);
            entity.translateY(v.getVy() * entity.timeDelta);
        }
        else {
            entity.translateX(entity.getVelocity().getVx() * entity.timeDelta);
            entity.translateY(entity.getVelocity().getVy() * entity.timeDelta);
        }

    }
}
