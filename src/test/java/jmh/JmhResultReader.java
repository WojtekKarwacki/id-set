package jmh;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JmhResultReader {


    public JmhResult getBenchmarkResult(String path) {
        File file = new File(path);
        ObjectMapper objectMapper = new ObjectMapper();
        BenchmarkResultDto[] benchmarkResultDtos = new BenchmarkResultDto[0];
        try {
            benchmarkResultDtos = objectMapper.readValue(file, objectMapper.getTypeFactory().constructArrayType(BenchmarkResultDto.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<BenchmarkResult> benchmarkResults = new ArrayList<>();
        for (BenchmarkResultDto benchmarkResultDto : benchmarkResultDtos) {
            WarmupTime warmupTime = new WarmupTime(Integer.parseInt(benchmarkResultDto.getWarmupTime().split(" ")[0]), TimeUnit.valueOf(benchmarkResultDto.getWarmupTime().split(" ")[1]));
            MeasurementTime measurementTime = new MeasurementTime(Integer.parseInt(benchmarkResultDto.getMeasurementTime().split(" ")[0]), TimeUnit.valueOf(benchmarkResultDto.getMeasurementTime().split(" ")[1]));
            PrimaryMetric primaryMetric = new PrimaryMetric(benchmarkResultDto.getPrimaryMetricDto().getScore(), benchmarkResultDto.getPrimaryMetricDto().getScoreError());
            BenchmarkResult benchmarkResult = new BenchmarkResult(benchmarkResultDto.getBenchmark(), warmupTime, benchmarkResultDto.getWarmupIterations(), measurementTime, benchmarkResultDto.getMeasurementIterations(), new Params(benchmarkResultDto.getParamsDto().getFunction(), Integer.parseInt(benchmarkResultDto.getParamsDto().getNumberOfElements()), benchmarkResultDto.getParamsDto().getTestObjectType()), primaryMetric);
            benchmarkResults.add(benchmarkResult);
        }
        JmhResult jmhResult = new JmhResult(benchmarkResults);
        return jmhResult;
    }
}
