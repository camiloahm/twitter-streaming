package org.interview.writer;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface OutputWriter {

    boolean write(Dataset<Row> dataSet);

}
