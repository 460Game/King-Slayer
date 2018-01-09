public class Player extends CollidingWorldObject<Player.PlayerData> {

    Player(Shape shape, GameModel model) {
        super(shape, model);
    }

    public static class PlayerData extends CollidingWorldObject.PositionWorldObjectData {

    }

    public void up() {
        this.data.shape.shift(0, -0.1);
    }

    public void left() {
        this.data.shape.shift(-0.1, 0);
    }

    public void right() {
        this.data.shape.shift(0.1, 0);
    }

    public void down() {
        this.data.shape.shift(0, 0.1);
    }
}
