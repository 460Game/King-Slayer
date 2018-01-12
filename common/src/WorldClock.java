/**
 * magical client/server synrnonzied clock
 * uses system time right now
 * TODO at start clients should adjust this to try to match servers time
 *
 * should only be used for deltas not absolutes
 */
public class WorldClock {
    public long serverStartTime;
    public long startTime;
    public long curTime;
    public WorldClock () {
        serverStartTime = 0;
        startTime = System.nanoTime();
    }
    public long nanoTime() {
        return serverStartTime + (System.nanoTime() - startTime);
    }
    public void sycronizeTime(long serverStartTime, long startTime, long curTime) {
        this.serverStartTime = serverStartTime;
        this.startTime = startTime;
        this.curTime = curTime;
    }
}
