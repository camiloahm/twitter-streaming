package org.interview.crawler;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

final class SparkSessionBuilderTest {

    @Test
    void shouldReturnSparkSessionWhenConfigNotNull() {
        SparkConf sparkConf = new SparkConf().setAppName("Read Text to RDD")
                .setMaster("local[2]")
                .set("spark.driver.allowMultipleContexts", "true")
                .set("spark.executor.memory", "2g");

        SparkSession sparkSession = SparkSessionBuilder.getInstance(sparkConf);
        assertThat(sparkSession, is(notNullValue()));
    }
}
