package jmh;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrimaryMetricDto {

    @JsonProperty("score")
    private double score;
    @JsonProperty("scoreError")
    private double scoreError;

    public double getScore() {
        return score;
    }

    public double getScoreError() {
        return scoreError;
    }
}
