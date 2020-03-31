package org.interview.crawler;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CrawlerConfig {

    @Value("${spark.app.name}")
    private String appName;

    @Value("${spark.batch.duration}")
    private Long batchDuration;

    @Value("${spark.master}")
    private String masterUri;

    @Bean
    SparkConf conf() {
        return new SparkConf()
                .setAppName(appName)
                .setMaster(masterUri)
                .set("spark.driver.allowMultipleContexts", "true");
    }

    @Bean
    JavaStreamingContext sc() {
        return new JavaStreamingContext(conf(), new Duration(batchDuration));
    }
}
