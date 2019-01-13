package jmh;

public class PrimaryMetric {

    private double score;
    private double scoreError;

    public PrimaryMetric(double score, double scoreError) {
        this.score = score;
        this.scoreError = scoreError;
    }

    public double getScore() {
        return score;
    }

    public double getScoreError() {
        return scoreError;
    }

}
