package jmh;

import java.util.List;

public class JmhResult {
    private List<BenchmarkResult> benchmarkResult;

    public JmhResult(List<BenchmarkResult> benchmarkResult) {
        this.benchmarkResult = benchmarkResult;
    }

    public List<BenchmarkResult> getBenchmarkResult() {
        return benchmarkResult;
    }
}
