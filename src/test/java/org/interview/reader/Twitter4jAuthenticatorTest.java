package org.interview.reader;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import twitter4j.auth.Authorization;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ReaderTest(profiles = "reader", imports = TwitterAuthenticatorConfig.class)
final class Twitter4jAuthenticatorTest {

    @Autowired
    private TwitterAuthenticator twitterAuthenticator;

    @Test
    void shouldInitializeProperties() {
        assertThat(twitterAuthenticator, is(notNullValue()));
    }

    @Test
    void shouldReturnAuthorizationIfAuthenticateSuccess() {
        TwitterAuthenticator twitterAuthenticator = mock(TwitterAuthenticator.class);
        Authorization mockAuthorization = mock(Authorization.class);
        when(twitterAuthenticator.authenticate()).thenReturn(mockAuthorization);
        assertThat(twitterAuthenticator.authenticate(), is(mockAuthorization));
    }

}
