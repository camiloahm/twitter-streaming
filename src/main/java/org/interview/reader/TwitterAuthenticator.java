package org.interview.reader;

import twitter4j.auth.Authorization;

interface TwitterAuthenticator {

    Authorization authenticate();
}
