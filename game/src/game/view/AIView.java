package game.view;

import game.model.Game.Model.GameModel;

public class AIView {

    //TODO

    Thread thread;

    public AIView(GameModel model) {
        thread = new Thread(() -> {
            model.update();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void start() {
        thread.start();
    }
}
