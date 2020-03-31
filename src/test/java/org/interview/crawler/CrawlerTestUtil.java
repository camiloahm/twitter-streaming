package org.interview.crawler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.interview.reader.TweetInfo;

import static org.interview.reader.TweetInfo.builder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class CrawlerTestUtil {

    static JavaDStream<TweetInfo> getTweetInfoDStream(final JavaDStream<String> tweetsRDD) {
        return tweetsRDD.map(line ->
        {
            String[] parts = line.split(",");
            TweetInfo tweet = builder()
                    .author(TweetInfo.Author.builder()
                            .id(Long.valueOf(parts[0].trim()))
                            .name(parts[1].trim())
                            .screenName(parts[2].trim())
                            .creationDate(Long.valueOf(parts[3].trim()))
                            .build()
                    )
                    .id(Long.valueOf(parts[4].trim()))
                    .textMessage(parts[5].trim())
                    .creationDate(Long.valueOf(parts[6].trim()))
                    .build();
            return tweet;
        });
    }

    static JavaRDD<TweetInfo> getJavaRddFromCSVRdd(final JavaRDD<String> tweetsRDD) {
        return tweetsRDD.map(line ->
        {
            String[] parts = line.split(",");
            TweetInfo tweet = builder()
                    .author(TweetInfo.Author.builder()
                            .id(Long.valueOf(parts[0].trim()))
                            .name(parts[1].trim())
                            .screenName(parts[2].trim())
                            .creationDate(Long.valueOf(parts[3].trim()))
                            .build()
                    )
                    .id(Long.valueOf(parts[4].trim()))
                    .textMessage(parts[5].trim())
                    .creationDate(Long.valueOf(parts[6].trim()))
                    .build();
            return tweet;
        });
    }

}


