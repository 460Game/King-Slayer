package game.model.game.model.worldObject.entity.slayer;

public class SlayerData {
    public final static int meleeSpeed = 8;
    public final static int meleeDamage = 10;

    public static SlayerData copyOf(SlayerData a) {
        SlayerData ret = new SlayerData();
        ret.meleeLastTime = a.meleeLastTime;
        ret.magic = a.magic;
        ret.meleeAngle = a.meleeAngle;
        return ret;
    }

    public double meleeLastTime = 0;
    public double meleeAngle = 0;
    public int magic = 100;

    public SlayerData() {}

}
