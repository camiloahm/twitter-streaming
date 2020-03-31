package org.interview.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.RequestToken;

import java.util.Scanner;

@Slf4j
@Service
final class Twitter4jAuthenticator implements TwitterAuthenticator {

    private final Twitter twitter;

    @Autowired
    public Twitter4jAuthenticator(final Twitter twitter) {
        this.twitter = twitter;
    }

    public Authorization authenticate() {
        try {
            String providedPin;
            RequestToken requestToken = twitter.getOAuthRequestToken();

            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Go to the following link in your browser:\n" + requestToken.getAuthorizationURL());
                System.out.println("\nPlease enter the retrieved PIN:");
                providedPin = scanner.nextLine();
            }

            if (providedPin == null || providedPin.isEmpty()) {
                throw new RuntimeException("Unable to read entered PIN");
            }

            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, providedPin);
            twitter.setOAuthAccessToken(accessToken);
            return twitter.getAuthorization();
        } catch (TwitterException e) {
            if (401 == e.getStatusCode()) {
                System.out.println("Unable to get the access token. Authorization denied");
            }
            throw new RuntimeException("Unexpected exception retrieving access token", e);
        }
    }
}
