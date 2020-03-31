package org.interview.writer;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LocalOutputWriter implements OutputWriter {

    private final String outputDirectory;

    @Autowired
    public LocalOutputWriter(@Value("${spark.app.outdir}") final String outputDirectory) {

        this.outputDirectory = outputDirectory;
    }

    @Override
    public boolean write(final Dataset<Row> dataSet) {
        dataSet.show();
        dataSet.write()
                .format("csv")
                .option("header", "true")
                .save(outputDirectory + "/" + System.currentTimeMillis() + ".csv");
        return true;
    }
}
