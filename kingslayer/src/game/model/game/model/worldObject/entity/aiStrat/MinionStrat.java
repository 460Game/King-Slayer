package game.model.game.model.worldObject.entity.aiStrat;


import game.message.toClient.SetEntityCommand;
import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;

public abstract class MinionStrat extends AIStrat {

    private MinionStrat() {

    }

    public static class RangedMinionStrat extends MinionStrat {

        public static final RangedMinionStrat SINGLETON = new RangedMinionStrat();


    }

    static class MinionStratAIData extends AIData {

        MinionStratAIData() {

        }
    }

    @Override
    public AIData makeAIData() {
        return new MinionStratAIData();
    }

    @Override
    public void updateAI(Entity entity, ServerGameModel model, double seconds) {
        entity.data.updateData.velocity.setVx((int) (Math.random() * 3) - 1);
        entity.data.updateData.velocity.setVy((int) (Math.random() * 3) - 1);
        model.processMessage(new SetEntityCommand(entity));
//        entity.data.x += entity.data.updateData.velocity.getVx() * seconds;
//        entity.data.y += entity.data.updateData.velocity.getVy() * seconds;
    }
}
