package game.model.game.model.worldObject.entity.updateStrat;

import game.model.game.model.worldObject.entity.entities.Velocity;

public class UpdateData {

    public Velocity velocity;
    public long lastUpdate;
    public double maxSpeed;

    UpdateData() {
        velocity = new Velocity();
        lastUpdate = -1;
        maxSpeed = 3;
    }
}