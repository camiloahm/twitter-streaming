package org.interview.crawler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.interview.reader.TweetInfo;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class DataFrameUtil {

    static StructType getTweetSchema() {
        return DataTypes.createStructType(
                new StructField[] {
                        DataTypes.createStructField("userId", DataTypes.LongType, true),
                        DataTypes.createStructField("userName", DataTypes.StringType, true),
                        DataTypes.createStructField("userScreenName", DataTypes.StringType, true),
                        DataTypes.createStructField("userCreationDate", DataTypes.LongType, true),
                        DataTypes.createStructField("textId", DataTypes.LongType, true),
                        DataTypes.createStructField("textMessage", DataTypes.StringType, true),
                        DataTypes.createStructField("textCreationDate", DataTypes.LongType, true),
                });
    }

    static JavaRDD<Row> buildRowRddFromTweetInfoRdd(final JavaRDD<TweetInfo> rdd) {
        return rdd.map(tweet ->
                RowFactory.create(
                        tweet.getAuthor().getId(),
                        tweet.getAuthor().getName(),
                        tweet.getAuthor().getScreenName(),
                        tweet.getAuthor().getCreationDate(),
                        tweet.getId(),
                        tweet.getTextMessage(),
                        tweet.getCreationDate()
                ));
    }
}
