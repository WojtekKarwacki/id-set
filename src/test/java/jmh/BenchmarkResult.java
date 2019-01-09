package jmh;

public class BenchmarkResult {

    private String benchmark;

    private WarmupTime warmupTime;
    private int warmupIterations;

    private MeasurementTime measurementTime;
    private int measurementIterations;

    private Params params;

    private PrimaryMetric primaryMetric;

    public BenchmarkResult(String benchmark, WarmupTime warmupTime, int warmupIterations, MeasurementTime measurementTime, int measurementIterations, Params params, PrimaryMetric primaryMetric) {
        this.benchmark = benchmark;
        this.warmupTime = warmupTime;
        this.warmupIterations = warmupIterations;
        this.measurementTime = measurementTime;
        this.measurementIterations = measurementIterations;
        this.params = params;
        this.primaryMetric = primaryMetric;
    }

    public String getBenchmark() {
        return benchmark;
    }

    public WarmupTime getWarmupTime() {
        return warmupTime;
    }

    public int getWarmupIterations() {
        return warmupIterations;
    }

    public MeasurementTime getMeasurementTime() {
        return measurementTime;
    }

    public int getMeasurementIterations() {
        return measurementIterations;
    }

    public Params getParams() {
        return params;
    }

    public PrimaryMetric getPrimaryMetric() {
        return primaryMetric;
    }
}
