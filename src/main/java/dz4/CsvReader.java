package dz4;

import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CsvReader {

    public List<Timestamp> readCsv() {

        try (CSVReader reader = new CSVReader(new FileReader("/Users/antee/Documents/Projects/Kotlin/KFU/sysanalysis/src/main/java/dz4/ddd.csv"))) {
            return reader.readAll().stream()
                    .map(arr -> Timestamp.valueOf(Arrays.stream(arr).reduce(String::concat).get()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
}
