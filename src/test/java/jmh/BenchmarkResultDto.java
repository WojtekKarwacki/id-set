package jmh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BenchmarkResultDto {

    @JsonProperty("benchmark")
    private String benchmark;

    @JsonProperty("warmupIterations")
    private int warmupIterations;
    @JsonProperty("warmupTime")
    private String warmupTime;

    @JsonProperty("measurementIterations")
    private int measurementIterations;
    @JsonProperty("measurementTime")
    private String measurementTime;

    @JsonProperty("params")
    private ParamsDto paramsDto;

    @JsonProperty("primaryMetric")
    private PrimaryMetricDto primaryMetricDto;

    public String getBenchmark() {
        return benchmark;
    }

    public int getWarmupIterations() {
        return warmupIterations;
    }

    public String getWarmupTime() {
        return warmupTime;
    }

    public int getMeasurementIterations() {
        return measurementIterations;
    }

    public String getMeasurementTime() {
        return measurementTime;
    }

    public ParamsDto getParamsDto() {
        return paramsDto;
    }

    public PrimaryMetricDto getPrimaryMetricDto() {
        return primaryMetricDto;
    }
}
