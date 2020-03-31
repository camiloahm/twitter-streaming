package org.interview.reader;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
class UserTweetReader implements TweetReader {

    private final TwitterAuthenticator twitterAuthenticator;
    private final String[] filter;

    public UserTweetReader(
            final TwitterAuthenticator twitterAuthenticator,
            @Value("${twitter.tweet.filter}") final String[] filter) {

        this.twitterAuthenticator = twitterAuthenticator;
        this.filter = filter;
    }

    @Override
    public JavaDStream<TweetInfo> readTweetsFromCurrentUser(final JavaStreamingContext streamingContext) {
        log.warn("Tweets filters by: ", Arrays.toString(filter));

        return TwitterUtils.createStream(streamingContext, twitterAuthenticator.authenticate(), filter)
                .map(status ->
                        TweetInfo.builder()
                                .id(status.getId())
                                .textMessage(status.getText())
                                .creationDate(status.getCreatedAt().getTime())
                                .author(
                                        TweetInfo.Author.builder()
                                                .id(status.getUser().getId())
                                                .name(status.getUser().getName())
                                                .screenName(status.getUser().getScreenName())
                                                .creationDate(status.getUser().getCreatedAt().getTime())
                                                .build())
                                .build());
    }

}
