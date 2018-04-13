package game.model.game.model.worldObject.entity.slayer;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Velocity;
import game.model.game.model.worldObject.entity.updateStrat.MovingStrat;

import static util.Const.AMOUNT_SLAYER_MANA_REGEN_UP;
import static util.Const.SECONDS_TO_SLAYER_MANA_REGEN_UP;

public class SlayerUpdateStrat extends MovingStrat {
    public static final SlayerUpdateStrat SINGLETON = new SlayerUpdateStrat();
    private SlayerUpdateStrat() {}

    @Override
    public void update(Entity entity, GameModel model) {
        SlayerData slayerData = SlayerData.copyOf(entity.get(Entity.EntityProperty.SLAYER_DATA));

        int time = (int) ((double) (model.nanoTime() - model.startTime) / 1000000000);


        // Idk why this doesnt work :(
        // Give slayer .25 extra mana regen rate for every 30 seconds in the game.
        if (time > 0 && time % SECONDS_TO_SLAYER_MANA_REGEN_UP == 0 && slayerData.readyToUpRegen) {
            slayerData.regenRate = slayerData.regenRate + AMOUNT_SLAYER_MANA_REGEN_UP;
            slayerData.readyToUpRegen = false;
        } else if ((time - 1) % SECONDS_TO_SLAYER_MANA_REGEN_UP == 0)
            slayerData.readyToUpRegen = true;

        if (slayerData.magic < 100) {
//            slayerData.magic += (slayerData.regenRate + 0.25 * (time / 30)) * entity.timeDelta;
            slayerData.magic += slayerData.regenRate * entity.timeDelta;
            if (slayerData.magic > 100) slayerData.magic = 100;
        }

        if (slayerData.meleeLastTime > 0) {

            slayerData.meleeLastTime -= entity.timeDelta;
            Velocity v = new Velocity().withMagnitude(SlayerData.meleeSpeed).withAngle(slayerData.meleeAngle);
            entity.translateX( v.getVx()
                    * entity.timeDelta);
            entity.translateY(v.getVy() * entity.timeDelta);
        }
        else {
            entity.translateX(entity.getVelocity().getVx() * entity.timeDelta);
            entity.translateY(entity.getVelocity().getVy() * entity.timeDelta);
        }
        entity.set(Entity.EntityProperty.SLAYER_DATA, slayerData);
    }
}
