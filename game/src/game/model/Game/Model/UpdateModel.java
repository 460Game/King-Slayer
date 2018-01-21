package game.model.Game.Model;

import Util.Const;
import game.model.Game.Model.Model;

public abstract class UpdateModel implements Model {

    private boolean running = false;

    public void start() {
        if (running) throw new RuntimeException("Cannot start model when already running");
        running = true;
        (new Thread(this::run)).start();
    }

    public void stop() {
        running = false;
    }

    public abstract void update();

    public boolean isRunning() {
        return running;
    }

    private void run() {
        while (running) {
            long start = System.nanoTime();
            this.update();
            long after = System.nanoTime();

            if (start + Const.UPDATE_LOOP_TIME_NANOS < after)
                try {
                    Thread.sleep(Const.UPDATE_LOOP_TIME_NANOS - after + start);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }
}
