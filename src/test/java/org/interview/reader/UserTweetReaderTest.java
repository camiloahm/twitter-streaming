package org.interview.reader;

import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ReaderTest(profiles = "reader", imports = TwitterAuthenticatorConfig.class)
final class UserTweetReaderTest {

    @Autowired
    private TweetReader tweetReader;

    @Test
    void shouldInitializeProperties() {
        assertThat(tweetReader, is(notNullValue()));
    }

    @Test
    void shouldReturnTweeterStreamWhenReadTweets() {
        TweetReader mockTweetReader = mock(UserTweetReader.class);
        JavaStreamingContext mockJavaStreamingContext = mock(JavaStreamingContext.class);
        JavaDStream<TweetInfo> mockTwitterStream = mock(JavaDStream.class);
        when(mockTweetReader.readTweetsFromCurrentUser(mockJavaStreamingContext)).thenReturn(mockTwitterStream);
        assertThat(mockTweetReader.readTweetsFromCurrentUser(mockJavaStreamingContext), is(mockTwitterStream));
    }
}
