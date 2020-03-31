package org.interview;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.interview.crawler.CrawlerRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class BieberApplication implements CommandLineRunner {

    private final CrawlerRunner crawlerRunner;

    public static void main(String[] args) {
        SpringApplication.run(BieberApplication.class, args);
    }

    @Override
    public void run(final String... args) {
        try {
            crawlerRunner.crawl();
        } catch (InterruptedException e) {
            log.error("Problem while terminating spark context", e);
            System.exit(0);
        } catch (Exception e) {
            log.error("Unexpected error in the app", e);
            System.exit(0);
        }
    }
}