package org.interview.reader;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import twitter4j.Twitter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ReaderTest(profiles = "reader", imports = TwitterAuthenticatorConfig.class)
final class TwitterAuthenticatorConfigTest {

    @Autowired
    private Twitter twitter;

    @Test
    void shouldInitializeProperties() {
        assertThat(twitter, is(notNullValue()));
    }
}
