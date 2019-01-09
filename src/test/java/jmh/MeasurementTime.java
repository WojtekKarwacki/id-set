package jmh;

public class MeasurementTime {

    private int time;
    private TimeUnit timeUnit;

    public MeasurementTime(int time, TimeUnit timeUnit) {
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
