package org.interview.reader;

import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public interface TweetReader {

    JavaDStream<TweetInfo> readTweetsFromCurrentUser(JavaStreamingContext streamingContext);
}
