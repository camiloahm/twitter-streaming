package org.interview.reader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

@Configuration
class TwitterAuthenticatorConfig {

    @Value("${twitter.oauth.consumer.key}")
    private String consumerKey;

    @Value("${twitter.oauth.consumer.secret}")
    private String consumerSecret;

    @Bean
    Twitter getTwitter4jConfig() {
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        return twitter;
    }
}
