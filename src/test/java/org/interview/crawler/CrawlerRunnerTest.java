package org.interview.crawler;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.interview.reader.TweetInfo;
import org.interview.reader.TweetReader;
import org.interview.writer.OutputWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.interview.crawler.CrawlerTestUtil.getTweetInfoDStream;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class CrawlerRunnerTest {

    private CrawlerRunner crawlerRunner;
    private JavaStreamingContext javaStreamingContext;
    private OutputWriter outputWriter;
    private TweetReader tweetReader;
    private SparkConf sparkConf;
    private SparkSession sparkSession;

    @BeforeEach
    void setUp() {
        sparkConf = new SparkConf().setAppName("Read Text to RDD")
                .set("spark.driver.allowMultipleContexts", "true")
                .setMaster("local[2]").set("spark.executor.memory", "2g");
        sparkSession = SparkSessionBuilder.getInstance(sparkConf);
        javaStreamingContext = new JavaStreamingContext(sparkConf, new Duration(10000));
        tweetReader = mock(TweetReader.class);
        outputWriter = mock(OutputWriter.class);

        crawlerRunner = new CrawlerRunner(tweetReader, outputWriter, javaStreamingContext, 10L, 10000L);
    }

    @Test
    void shouldReturnTrueAfterCrawlingIsSuccess() throws InterruptedException {

        String tweet1 = "54263607,Lilo Oli,Drieazu,1246899768000,1223140610099556352,\"RT @jackieaina: *Bidi Bidi Bom Bom comes on at the club\", 1580454420000\n";
        String tweet2 = "2768477940,Jazmine,jazzyjaz_xo,1409012070000,1223140611290624000,RT @2020thoughts_: Thread on how Justin Bieber cheated and abused Selena Gomez. https://t.co/HhR4tTq1it,1580454421000\n";
        List<String> list = Stream.of(tweet1, tweet2).collect(Collectors.toList());
        Dataset<String> listDS = sparkSession.createDataset(list, Encoders.STRING());
        JavaRDD<String> javaRDDString = listDS.toJavaRDD();
        Queue<JavaRDD<String>> rddQueue = new LinkedList<>();
        rddQueue.add(javaRDDString);
        JavaDStream<String> records = javaStreamingContext.queueStream(rddQueue);
        JavaDStream<TweetInfo> tweetsDStream = getTweetInfoDStream(records);

        when(tweetReader.readTweetsFromCurrentUser(javaStreamingContext)).thenReturn(tweetsDStream);
        assertThat(crawlerRunner.crawl(), is(true));
    }

}
