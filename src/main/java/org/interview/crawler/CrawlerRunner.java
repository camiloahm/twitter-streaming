package org.interview.crawler;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.interview.reader.TweetInfo;
import org.interview.reader.TweetReader;
import org.interview.writer.OutputWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.interview.crawler.DataFrameUtil.buildRowRddFromTweetInfoRdd;
import static org.interview.crawler.DataFrameUtil.getTweetSchema;

@Slf4j
@Component
public class CrawlerRunner {

    private final TweetReader tweetReader;
    private final OutputWriter outputWriter;
    private final JavaStreamingContext streamingContext;
    private final Long numberTweetsToCollect;
    private final Long timeout;

    @Autowired
    public CrawlerRunner(
            final TweetReader tweetReader,
            final OutputWriter outputWriter, final JavaStreamingContext streamingContext,
            @Value("${spark.app.tweetsLimit}") final Long numberTweetsToCollect,
            @Value("${spark.app.timeout}") final Long timeout) {

        this.tweetReader = tweetReader;
        this.outputWriter = outputWriter;
        this.streamingContext = streamingContext;
        this.numberTweetsToCollect = numberTweetsToCollect;
        this.timeout = timeout;
    }

    /**
     * Execution Steps: WriteOutput(processInfo(readTweets()))
     *
     * 1-> Get Tweets from Twitter
     * 2-> Process tweets until reach the number of Tweets to collect or reach the timeout configured
     * 3-> Write tweets using the writer configure (Local/S3/etc).
     */
    public boolean crawl() throws InterruptedException {

        final JavaDStream<TweetInfo> filteredTweets = tweetReader.readTweetsFromCurrentUser(streamingContext);

        filteredTweets
                .foreachRDD(rdd -> {
                    long numTweetsCollected = 0;
                    long numberOfTweets = rdd.count();

                    if (numberOfTweets > 0) {
                        numTweetsCollected += numberOfTweets;

                        if (numTweetsCollected <= numberTweetsToCollect) {
                            SparkSession spark = SparkSessionBuilder.getInstance(rdd.context().getConf());
                            Dataset<Row> tweetDataFrame = spark.createDataFrame(buildRowRddFromTweetInfoRdd(rdd), getTweetSchema());
                            Dataset<Row> sortedTweets = tweetDataFrame.orderBy("userCreationDate", "textCreationDate");
                            outputWriter.write(sortedTweets);
                        } else {
                            streamingContext.stop();
                        }
                    }
                });

        streamingContext.start();
        streamingContext.awaitTerminationOrTimeout(timeout);

        return true;
    }
}