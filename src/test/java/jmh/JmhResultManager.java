package jmh;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class JmhResultManager {
    private JmhResultReader jmhResultReader;
    private JmhResultCsvConverter jmhResultCsvConverter;

    public JmhResultManager() {
        jmhResultReader = new JmhResultReader();
        jmhResultCsvConverter = new JmhResultCsvConverter();
    }

    public void handleResult() {
        String now = LocalDateTime.now().toString().replaceAll(":", "-").replaceAll(Pattern.quote("."), "-");
        String directoryPath = "benchmarkResults" + File.separator + now;
        String resultPath = directoryPath + File.separator + "jmh-result.json";
        new File(directoryPath).mkdirs();
        try {
            Files.move(Paths.get("jmh-result.json"), Paths.get(resultPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JmhResult jmhResult = jmhResultReader.getBenchmarkResult(resultPath);
        try (FileWriter fileWriter = new FileWriter(directoryPath + File.separator + "jmh-result.csv")) {
            fileWriter.write(jmhResultCsvConverter.convert(jmhResult));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
