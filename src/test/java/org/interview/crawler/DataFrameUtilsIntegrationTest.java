package org.interview.crawler;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.interview.reader.TweetInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.interview.crawler.CrawlerTestUtil.getJavaRddFromCSVRdd;
import static org.interview.crawler.DataFrameUtil.getTweetSchema;

final class DataFrameUtilsIntegrationTest {

    @Test
    void shouldBuildRowJavaRddWhenGivenJavaRdd() {

        SparkConf sparkConf = new SparkConf().setAppName("Read Text to RDD")
                .setMaster("local[2]")
                .set("spark.driver.allowMultipleContexts", "true")
                .set("spark.executor.memory", "2g");

        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        JavaRDD<String> tweetsRDD = sc.textFile("src/test/resources/data/tweets.csv");
        JavaRDD<TweetInfo> tweetInfoJavaRDD = getJavaRddFromCSVRdd(tweetsRDD);
        JavaRDD<Row> rowJavaRDD = DataFrameUtil.buildRowRddFromTweetInfoRdd(tweetInfoJavaRDD);
        List<Row> tweets = rowJavaRDD.collect();
        assertThat(tweets, not(empty()));
    }

    @Test
    void shouldBuildDataSetWithProperRdd() {

        SparkConf sparkConf = new SparkConf().setAppName("Read Text to RDD")
                .set("spark.driver.allowMultipleContexts", "true")
                .setMaster("local[2]").set("spark.executor.memory", "2g");

        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        JavaRDD<String> tweetsRDD = sc.textFile("src/test/resources/data/tweets.csv");
        JavaRDD<TweetInfo> tweetInfoJavaRDD = getJavaRddFromCSVRdd(tweetsRDD);
        JavaRDD<Row> rowJavaRDD = DataFrameUtil.buildRowRddFromTweetInfoRdd(tweetInfoJavaRDD);
        SparkSession spark = SparkSessionBuilder.getInstance(tweetInfoJavaRDD.context().getConf());
        Dataset<Row> tweetDataFrame = spark.createDataFrame(rowJavaRDD, getTweetSchema());
        assertThat(tweetDataFrame.count(), greaterThan(0L));
    }

}
