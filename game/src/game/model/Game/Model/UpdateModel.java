package game.model.Game.Model;

import com.esotericsoftware.minlog.Log;

import static Util.Const.*;

public abstract class UpdateModel implements Model {

    private boolean running = false;

    public void start() {
        Log.info("Starting Model");
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
            long delta = System.nanoTime()- start;
            if (UPDATE_LOOP_TIME_NANOS > delta)
                try {
                Thread.yield();
                    Thread.sleep((UPDATE_LOOP_TIME_NANOS - delta)/ 1000000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }
}
