package org.interview.crawler;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

final class SparkSessionBuilder {

    private static transient SparkSession instance;

    public static SparkSession getInstance(final SparkConf sparkConf) {
        if (instance == null) {
            instance = SparkSession
                    .builder()
                    .config(sparkConf)
                    .getOrCreate();
        }
        return instance;
    }
}
