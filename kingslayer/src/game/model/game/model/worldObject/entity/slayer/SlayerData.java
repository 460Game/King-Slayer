package game.model.game.model.worldObject.entity.slayer;

public class SlayerData {
    public final static int meleeSpeed = 8;
    public final static int meleeDamage = 20;
    public final static int arrowCost = 5;
    public final static double arrowDamage = 5;
    public final static int meleeCost = 10;

    public static SlayerData copyOf(SlayerData a) {
        SlayerData ret = new SlayerData();
        ret.meleeLastTime = a.meleeLastTime;
        ret.magic = a.magic;
        ret.meleeAngle = a.meleeAngle;
        ret.regenRate = a.regenRate;
        ret.readyToUpRegen = a.readyToUpRegen;
        return ret;
    }

    public double meleeLastTime = 0;
    public double meleeAngle = 0;
    public double magic = 100;

    public boolean readyToUpRegen = false;
    public double regenRate = 4;

    public SlayerData() {}

    public void setRegenRate(double rate) { this.regenRate = rate; }

}
