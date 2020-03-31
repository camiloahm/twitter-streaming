package org.interview.crawler;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.interview.reader.TweetReader;
import org.interview.writer.OutputWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@CrawlerTest(profiles = "crawler", imports = CrawlerConfig.class)
final class CrawlerConfigTest {

    @Autowired
    private SparkConf sparkConf;

    @Autowired
    private JavaStreamingContext javaStreamingContext;

    @MockBean
    private OutputWriter outputWriter;

    @MockBean
    private TweetReader tweetReader;

    @MockBean
    private CrawlerRunner crawlerRunner;

    @Test
    void shouldInitializeProperties() {

        assertThat(sparkConf, is(notNullValue()));
        assertThat(javaStreamingContext, is(notNullValue()));
        assertThat(tweetReader, is(notNullValue()));
        assertThat(outputWriter, is(notNullValue()));
        assertThat(crawlerRunner, is(notNullValue()));
    }
}
