package jmh;

public class WarmupTime {
    private int time;
    private TimeUnit timeUnit;

    public WarmupTime(int time, TimeUnit timeUnit) {
        this.time = time;
        this.timeUnit = timeUnit;
    }

    public int getTime() {
        return time;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
