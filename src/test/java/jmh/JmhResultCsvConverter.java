package jmh;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

public class JmhResultCsvConverter {

    public static void main(String[] args) throws IOException {
        JmhResultCsvConverter jmhResultCsvConverter = new JmhResultCsvConverter();
        FileWriter fileWriter = new FileWriter("benchmarkResults\\2019-01-06T03-17-09-039\\jmh-result.csv");
        fileWriter.write(jmhResultCsvConverter.convert(new JmhResultReader().getBenchmarkResult("benchmarkResults\\2019-01-06T03-17-09-039\\jmh-result.json")));
        fileWriter.close();
    }

    public String convert(JmhResult jmhResult) {
        StringBuilder csv = new StringBuilder();
        csv.append("benchmark;function;numberOfElements;testObjectType;Score"+System.getProperty("line.separator"));
        for (BenchmarkResult benchmarkResult : jmhResult.getBenchmarkResult()) {
            csv
                    .append(benchmarkResult.getBenchmark()).append(";")
                    .append(benchmarkResult.getParams().getFunction()).append(";")
                    .append(benchmarkResult.getParams().getNumberOfElements()).append(";")
                    .append(benchmarkResult.getParams().getTestObjectType()).append(";")
                    .append(BigDecimal.valueOf(benchmarkResult.getPrimaryMetric().getScore()).setScale(2, BigDecimal.ROUND_DOWN)).append(System.getProperty("line.separator"))
            ;
        }
        return csv.toString();
    }
}
