package org.interview.reader;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public final class TweetInfo implements Serializable {

    private Author author;
    private Long id;
    private String textMessage;
    private Long creationDate;

    @Getter
    @Builder
    public static class Author implements Serializable {

        private Long id;
        private String name;
        private String screenName;
        private Long creationDate;

    }
}
