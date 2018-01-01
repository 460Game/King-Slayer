/**
 * magical client/server synrnonzied clock
 * uses system time right now
 * TODO at start clients should adjust this to try to match servers time
 *
 * should only be used for deltas not absolutes
 */
public interface WorldClock {
    public abstract long nanoTime();
}
